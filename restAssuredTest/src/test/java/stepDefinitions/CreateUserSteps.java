package stepDefinitions;

import br.com.projeto.pojos.UserResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.E;
import io.restassured.RestAssured;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

import java.util.List;
import java.util.Map;

public class CreateUserSteps {
    private RequestSpecification request;
    private Response response;

    String baseUrl = "https://reqres.in/api";

    @Dado("que o usuário possui dados de criação")
    public void queOUsuarioPossuiDadosDeCriacao() {
        request = RestAssured.given();
    }

    @Quando("o usuário faz uma requisição POST para {string} com os seguintes dados:")
    public void oUsuarioFazRequisicaoPost(String endpoint, DataTable dataTable) {
        List<Map<String, String>> dadosUsuario = dataTable.asMaps(String.class, String.class);
        Map<String, String> usuario = dadosUsuario.get(0);

        request.header("Content-Type", "application/json");
        request.body(usuario);
        response = request.post(baseUrl + endpoint);
    }

    @Então("a resposta deve ter o código de status {int}")
    public void aRespostaDeveTerCodigoDeStatus(int codigoStatusEsperado) {
        int codigoStatusAtual = response.getStatusCode();
        Assert.assertEquals(codigoStatusEsperado, codigoStatusAtual);
    }

    @E("a resposta deve conter os campos obrigatórios")
    public void aRespostaDeveConterCamposObrigatorios() {
        UserResponse userResponse = response.as(UserResponse.class);

        // Verificar se os campos obrigatórios estão preenchidos no objeto Users
        Assert.assertNotNull("O campo 'name' é obrigatório.", userResponse.getName());
        Assert.assertNotNull("O campo 'job' é obrigatório.", userResponse.getJob());
        Assert.assertNotNull("O campo 'id' é obrigatório.", userResponse.getId());
        Assert.assertNotNull("O campo 'createdAt' é obrigatório.", userResponse.getCreatedAt());
    }

    @E("a resposta deve seguir o contrato")
    public void aRespostaDeveSeguirContrato() {
        try {
            JsonNode userResponseSchema = JsonLoader.fromResource("/json/userResponseSchema.json");
            JsonNode responseJson = JsonLoader.fromString(response.getBody().asString());

            JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
            JsonSchema schema = factory.getJsonSchema(userResponseSchema);

            schema.validate(responseJson);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("A resposta não segue o contrato.");
        }
    }
}
