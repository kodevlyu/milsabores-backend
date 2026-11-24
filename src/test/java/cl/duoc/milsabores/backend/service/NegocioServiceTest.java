package cl.duoc.milsabores.backend.service;

import cl.duoc.milsabores.backend.dto.*;
import cl.duoc.milsabores.backend.entity.*;
import cl.duoc.milsabores.backend.exception.*;
import cl.duoc.milsabores.backend.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class NegocioServiceTest {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BoletaService boletaService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BoletaRepository boletaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Long categoriaId;
    private Long productoId;
    private Long usuarioId;

    @BeforeEach
    void prepararDatos() {

        CategoriaResponse categoria =
            categoriaService.crear(
                new CategoriaRequest(
                    "Tortas de prueba",
                    "Categoria para testing",
                    true
                )
            );

        categoriaId = categoria.id();

        ProductoResponse producto =
            productoService.crear(
                new ProductoRequest(
                    "TEST001",
                    "Torta de Chocolate",
                    "Producto de prueba",
                    new BigDecimal("15000.00"),
                    10,
                    null,
                    true,
                    categoriaId
                )
            );

        productoId = producto.id();

        UsuarioResponse usuario =
            usuarioService.registrar(
                new UsuarioRequest(
                    "123456785",
                    "Cliente",
                    "Prueba",
                    "testing@example.com",
                    "Prueba123!",
                    "Metropolitana",
                    "Melipilla",
                    "Direccion de prueba"
                )
            );

        usuarioId = usuario.id();
    }

    @Test
    void debeCrearCategoria() {

        CategoriaResponse categoria =
            categoriaService.buscarPorId(categoriaId);

        assertEquals(
            "Tortas de prueba",
            categoria.nombre()
        );

        assertTrue(categoria.activo());
    }

    @Test
    void debeCrearProducto() {

        ProductoResponse producto =
            productoService.buscarPorId(productoId);

        assertEquals(
            "Torta de Chocolate",
            producto.nombre()
        );

        assertEquals(10, producto.stock());
    }

    @Test
    void debeRegistrarUsuarioComoCliente() {

        UsuarioResponse usuario =
            usuarioService.buscarPorId(usuarioId);

        assertEquals(Rol.CLIENTE, usuario.rol());

        assertTrue(usuario.activo());
    }

    @Test
    void debeProtegerPasswordConBCrypt() {

        Usuario usuario = usuarioRepository
            .findById(usuarioId)
            .orElseThrow();

        assertNotEquals(
            "Prueba123!",
            usuario.getPasswordHash()
        );

        assertTrue(
            passwordEncoder.matches(
                "Prueba123!",
                usuario.getPasswordHash()
            )
        );
    }

    @Test
    void debeRechazarCorreoDuplicado() {

        assertThrows(
            ConflictoException.class,
            () -> usuarioService.registrar(
                new UsuarioRequest(
                    "98765433",
                    "Otro",
                    "Cliente",
                    "testing@example.com",
                    "Prueba123!",
                    "Metropolitana",
                    "Melipilla",
                    null
                )
            )
        );
    }

    @Test
    void debeCalcularTotalYDescontarStock() {

        BoletaRequest request = new BoletaRequest(
            List.of(
                new DetalleBoletaRequest(
                    productoId,
                    2
                )
            )
        );

        BoletaResponse boleta =
            boletaService.crear(usuarioId, request);

        assertNotNull(boleta.id());

        assertEquals(
            0,
            new BigDecimal("30000.00")
                .compareTo(boleta.total())
        );

        assertEquals(
            EstadoBoleta.CONFIRMADA,
            boleta.estado()
        );

        assertEquals(1, boleta.detalles().size());

        Producto producto = productoRepository
            .findById(productoId)
            .orElseThrow();

        assertEquals(8, producto.getStock());

        assertTrue(
            boletaRepository.existsById(boleta.id())
        );
    }

    @Test
    void debeRechazarStockInsuficiente() {

        BoletaRequest request = new BoletaRequest(
            List.of(
                new DetalleBoletaRequest(
                    productoId,
                    100
                )
            )
        );

        assertThrows(
            ConflictoException.class,
            () -> boletaService.crear(
                usuarioId,
                request
            )
        );
    }

    @Test
    void debeRechazarCantidadInvalida() {

        BoletaRequest request = new BoletaRequest(
            List.of(
                new DetalleBoletaRequest(
                    productoId,
                    0
                )
            )
        );

        assertThrows(
            SolicitudInvalidaException.class,
            () -> boletaService.crear(
                usuarioId,
                request
            )
        );
    }

    @Test
    void debeDetectarProductoInexistente() {

        assertThrows(
            RecursoNoEncontradoException.class,
            () -> productoService.buscarPorId(
                Long.MAX_VALUE
            )
        );
    }

    @Test
    void debeDesactivarProducto() {

        productoService.eliminar(productoId);

        ProductoResponse producto =
            productoService.buscarPorId(productoId);

        assertFalse(producto.disponible());
    }
}