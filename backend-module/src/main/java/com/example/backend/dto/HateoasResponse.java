package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Resposta padrao da API com links de navegacao")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HateoasResponse<T> extends ApiResponseDTO<T> {

    @Schema(description = "Links de navegacao")
    private List<LinkInfo> links = new ArrayList<>();

    public HateoasResponse() {}

    public HateoasResponse(boolean success, String message, T data) {
        super(success, message, data);
    }

    public static <T> HateoasResponse<T> ok(String message, T data) {
        return new HateoasResponse<>(true, message, data);
    }

    public HateoasResponse<T> addLink(String rel, String href, String method) {
        this.links.add(new LinkInfo(rel, href, method));
        return this;
    }

    public List<LinkInfo> getLinks() { return links; }
    public void setLinks(List<LinkInfo> links) { this.links = links; }

    @Schema(description = "Link de navegacao")
    public static class LinkInfo {
        private String rel;
        private String href;
        private String method;

        public LinkInfo() {}

        public LinkInfo(String rel, String href, String method) {
            this.rel = rel;
            this.href = href;
            this.method = method;
        }

        public String getRel() { return rel; }
        public void setRel(String rel) { this.rel = rel; }
        public String getHref() { return href; }
        public void setHref(String href) { this.href = href; }
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
    }
}
