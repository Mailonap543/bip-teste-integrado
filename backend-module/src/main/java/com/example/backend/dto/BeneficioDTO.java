package com.example.backend.dto;

public class BeneficioDTO {


    private Long fromId;
    private Long toId;
    private Double amount;

    private Long id;
    private String nome;
    private String descricao;

    private Double valor;


    private Boolean ativa;




    public Long getFromId() { return fromId; }
    public void setFromId(Long fromId) { this.fromId = fromId; }

    public Long getToId() { return toId; }
    public void setToId(Long toId) { this.toId = toId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public Boolean getAtiva() { return ativa; }
    public void setAtiva(Boolean ativa) { this.ativa = ativa; }



    public static class Beneficio {
        private Long id;
        private String nome;
        private String descricao;


        private double saldo;
        private boolean ativa;

        private Double valor;




        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }

        public double getSaldo() { return saldo; }
        public void setSaldo(double saldo) { this.saldo = saldo; }


        public boolean isAtiva() { return ativa; }
        public void setAtiva(boolean ativa) { this.ativa = ativa; }

        public Double getValor() { return valor; }
        public void setValor(Double valor) { this.valor = valor; }
    }
}