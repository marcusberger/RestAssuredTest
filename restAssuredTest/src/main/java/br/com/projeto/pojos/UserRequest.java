package br.com.projeto.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("job")
    private String job;

}
