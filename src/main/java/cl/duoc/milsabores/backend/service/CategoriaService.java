package cl.duoc.milsabores.backend.service;

import cl.duoc.milsabores.backend.dto.*;
import cl.duoc.milsabores.backend.entity.Categoria;
import cl.duoc.milsabores.backend.repository.CategoriaRepository;
import cl.duoc.milsabores.backend.exception.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return repository.findAll()
                .stream()
                .map(this::convertir)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarActivas() {
        return repository.findByActivoTrue()
                .stream()
                .map(this::convertir)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscarPorId(Long id) {
        return convertir(obtenerEntidad(id));
    }

    public CategoriaResponse crear(CategoriaRequest request) {

        if (repository.existsByNombre(request.nombre())) {
            throw new ConflictoException(
                "La categoria ya existe"
            );
        }

        Categoria categoria = new Categoria();

        categoria.setNombre(request.nombre());
        categoria.setDescripcion(request.descripcion());
        categoria.setActivo(
            request.activo() == null || request.activo()
        );

        return convertir(repository.save(categoria));
    }

    public CategoriaResponse actualizar(
            Long id, CategoriaRequest request) {

        Categoria categoria = obtenerEntidad(id);

        repository.findByNombre(request.nombre())
            .filter(c -> !c.getId().equals(id))
            .ifPresent(c -> {
                throw new ConflictoException(
                    "El nombre ya esta registrado"
                );
            });

        categoria.setNombre(request.nombre());
        categoria.setDescripcion(request.descripcion());

        if (request.activo() != null) {
            categoria.setActivo(request.activo());
        }

        return convertir(repository.save(categoria));
    }

    public void eliminar(Long id) {
        Categoria categoria = obtenerEntidad(id);
        categoria.setActivo(false);
        repository.save(categoria);
    }

    private Categoria obtenerEntidad(Long id) {
        return repository.findById(id)
            .orElseThrow(() ->
                new RecursoNoEncontradoException(
                    "Categoria no encontrada"
                )
            );
    }

    private CategoriaResponse convertir(Categoria c) {
        return new CategoriaResponse(
            c.getId(),
            c.getNombre(),
            c.getDescripcion(),
            c.isActivo()
        );
    }
}