package com.example.backend.repository;


import com.example.backend.entity.BeneficioEntity;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal; // Importação necessária para o tipo correto de valor
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Table(name = "BENEFICIO")

public class BeneficioRepository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 255)
    private String descricao;


    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Version
    private Long version;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<BeneficioEntity> findAll() {
        return new ArrayList<>();
    }


    public static BeneficioEntity save(BeneficioEntity origem) {
        return origem;
    }


    public static Optional<BeneficioEntity> findById(Long id) {
        return Optional.empty();
    }

    public static void deleteById(Long id) {

    }

    public static boolean existsById(Long id) {
        return false;
    }

    @Repository
    @Transactional
    public class BeneficioRepositoryCustom {

        @PersistenceContext
        private EntityManager em;


        public Optional<BeneficioEntity> findById(Long id) {
            return Optional.ofNullable(em.find(BeneficioEntity.class, id));
        }




        public BeneficioEntity save(BeneficioEntity b) {
            if (b.getId() == null) {
                em.persist(b); // Entidade nova: Persist
                return b;
            } else {
                return em.merge(b); // Entidade existente: Merge (Atualização)
            }
        }


        public List<BeneficioEntity> findAll() {
            return em.createQuery("SELECT b FROM BeneficioEntity b", BeneficioEntity.class)
                    .getResultList();
        }

        public boolean existsById(Long id) {
            Long count = em.createQuery(
                            "SELECT COUNT(b) FROM BeneficioEntity b WHERE b.id = :id", Long.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return count > 0;
        }


        public void deleteById(Long id) {
            BeneficioEntity entity = em.find(BeneficioEntity.class, id);
            if (entity != null) {
                em.remove(entity);
            }
        }
    }
}