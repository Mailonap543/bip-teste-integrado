package com.example.backend.controller;

import com.example.backend.annotation.Audited;
import com.example.backend.dto.ApiResponseDTO;
import com.example.backend.dto.BeneficioDTO;
import com.example.backend.dto.PagedResponse;
import com.example.backend.entity.BeneficioEntity;
import com.example.backend.service.BeneficioService;
import com.example.backend.validation.OnCreate;
import com.example.backend.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.backend.constants.ApiConstants.MAX_PAGE_SIZE;

@RestController
@RequestMapping("/api/v1/beneficios")
@Validated
@Tag(name = "Beneficios v1", description = "API v1 para gestao de beneficios")
public class BeneficioController {

    private static final Logger log = LoggerFactory.getLogger(BeneficioController.class);
    private static final String PATH_ID = "/{id}";
    private final BeneficioService service;

    public BeneficioController(BeneficioService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos os beneficios")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<ApiResponseDTO<List<BeneficioEntity>>> listAll() {
        log.info("Listing all beneficios");
        List<BeneficioEntity> list = service.listAll();
        return ResponseEntity.ok(ApiResponseDTO.ok("Beneficios listados com sucesso", list));
    }

    @GetMapping("/paged")
    @Operation(summary = "Listar beneficios com paginacao")
    @ApiResponse(responseCode = "200", description = "Pagina retornada com sucesso")
    public ResponseEntity<ApiResponseDTO<PagedResponse<BeneficioEntity>>> listPaged(
            @Parameter(description = "Numero da pagina (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Tamanho da pagina") @RequestParam(defaultValue = "20") @Min(1) int size,
            @Parameter(description = "Campo para ordenacao") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Direcao da ordenacao (asc/desc)") @RequestParam(defaultValue = "asc") String direction) {

        int pageSize = Math.min(size, MAX_PAGE_SIZE);
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<BeneficioEntity> result = service.listPaged(pageable);
        PagedResponse<BeneficioEntity> pagedResponse = PagedResponse.from(result);
        return ResponseEntity.ok(ApiResponseDTO.ok("Beneficios listados com sucesso", pagedResponse));
    }

    @GetMapping(PATH_ID)
    @Operation(summary = "Obter beneficio por ID")
    @ApiResponse(responseCode = "200", description = "Beneficio encontrado")
    @ApiResponse(responseCode = "400", description = "Beneficio nao encontrado")
    public ResponseEntity<ApiResponseDTO<BeneficioEntity>> get(
            @Parameter(description = "ID do beneficio") @PathVariable @Min(1) Long id) {
        log.info("Getting beneficio by id: {}", id);
        BeneficioEntity b = service.get(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Beneficio encontrado", b));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Criar novo beneficio",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Exemplo de criacao",
                                    value = "{\"nome\": \"Maria Silva\", \"saldo\": 3000.00, \"ativa\": true, \"descricao\": \"Vale Alimentacao\"}"
                            )
                    )
            )
    )
    @ApiResponse(responseCode = "201", description = "Beneficio criado")
    @ApiResponse(responseCode = "400", description = "Dados invalidos")
    @Audited(action = "CREATE_BENEFICIO", entity = "Beneficio")
    public ResponseEntity<ApiResponseDTO<BeneficioEntity>> create(
            @Valid @org.springframework.validation.annotation.Validated(OnCreate.class) @RequestBody BeneficioDTO dto) {
        log.info("Creating beneficio: {}", dto.getNome());
        BeneficioEntity b = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Beneficio criado com sucesso", b));
    }

    @PutMapping(PATH_ID)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Atualizar beneficio")
    @ApiResponse(responseCode = "200", description = "Beneficio atualizado")
    @ApiResponse(responseCode = "400", description = "Dados invalidos ou beneficio nao encontrado")
    @Audited(action = "UPDATE_BENEFICIO", entity = "Beneficio")
    public ResponseEntity<ApiResponseDTO<BeneficioEntity>> update(
            @PathVariable @Min(1) Long id,
            @Valid @org.springframework.validation.annotation.Validated(OnUpdate.class) @RequestBody BeneficioDTO dto) {
        log.info("Updating beneficio id: {}", id);
        BeneficioEntity b = service.update(id, dto);
        return ResponseEntity.ok(ApiResponseDTO.ok("Beneficio atualizado com sucesso", b));
    }

    @DeleteMapping(PATH_ID)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remover beneficio")
    @ApiResponse(responseCode = "200", description = "Beneficio removido")
    @ApiResponse(responseCode = "400", description = "Beneficio nao encontrado")
    @Audited(action = "DELETE_BENEFICIO", entity = "Beneficio")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable @Min(1) Long id) {
        log.info("Deleting beneficio id: {}", id);
        service.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Beneficio removido com sucesso", null));
    }

    @PostMapping("/transfer/{origemId}/{destinoId}/{valor}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Transferir valor entre beneficios",
            description = "Transfere um valor do beneficio de origem para o beneficio de destino"
    )
    @ApiResponse(responseCode = "200", description = "Transferencia realizada")
    @ApiResponse(responseCode = "400", description = "Erro na transferencia")
    @Audited(action = "TRANSFER", entity = "Transference")
    public ResponseEntity<ApiResponseDTO<Void>> transfer(
            @PathVariable @Min(1) Long origemId,
            @PathVariable @Min(1) Long destinoId,
            @PathVariable Double valor) {
        log.info("Transfer request: from {} to {} amount {}", origemId, destinoId, valor);
        service.transfer(origemId, destinoId, valor);
        return ResponseEntity.ok(ApiResponseDTO.ok("Transferencia realizada com sucesso", null));
    }
}
