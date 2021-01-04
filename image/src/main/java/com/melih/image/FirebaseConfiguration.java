package com.melih.image;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.client.util.Value;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

/*
@Configuration
public class FirebaseConfiguration {

	  private String gservicesConfig = "{\r\n" + 
	  		"  \"type\": \"service_account\",\r\n" + 
	  		"  \"project_id\": \"nesnealgilama\",\r\n" + 
	  		"  \"private_key_id\": \"8bf536abdbac633e701ebcb5910d24f2856d65e4\",\r\n" + 
	  		"  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC44n0HOWJtlVex\\n/cfxBOiDPKuDg87IMXY3UetdarHwjx9DAtgtVbRZC01nHYt2AV4w1/dv72rbEcJz\\n2B6W3K3/zQU44kPUfrwjXjtymkXG7Hu1wP/njT1XdJnyd0dd2VvwwuLIUGIZQWCX\\namcNSDcnT1N5fJsK84z7KjoRvxx5bBjka5O8MUelQfaTZxQLQDD4d+DIkdU21sRZ\\nbnSW1SSCvKtWCZ9Ya74Fi27BLfoH0WVXeVEtKm3YBRCTIcCvrXTjzv/58OOhY+uh\\nydub/um42p+eJh75OqhV7iYvv15CK5lY07NCXOZ34zxryAxygwfcJ2rsBvZIk0AX\\nYQx4SgjJAgMBAAECggEAAUiZQ56TX11lEXhIlhTJpHecXTpiCiPFxVaNQsXF3Hyx\\n4942MAJjUSbx76BV4ojZ5Y7qSd2oqUKzTeiXNcbsclUzDxULEYObvMcftIj7qQjl\\nRvrDDBF/9oZ5tyQ625zxtXc06d0dAT3h9MspUf9JWr2DP9msnePY+7lsU8k24mTp\\ns7P/22Ny9yoPjj3OEIW0rg1yieehBSIxAkO7TaaS2Z8eEZWQQCMIYSrQvL3kTee4\\nO83jMCVQTMv7q4qUKUjzY4NHYVlqB7JEYXGCOrjQjpnuubClRb4rhUKX2T2JV91Z\\nc9DBivBtrKZDxFyBJyAme1UtPBs0JLeXu5p4Oya3XQKBgQDq7drKXpmVfQ1ruMSk\\nMoDGT4JCVErtjpqHYBadCC7WA5f5T9LtMObh0F643RPm4V+gNgiP0TEb3Ap0rxCC\\nQ+0C6Kg2j40XkQK9DEYbsK0QPSUl7l/g3JaBh7nj34kX7PXvOp/rwcliXLfEz6rq\\nTcDXiQOrblEvBAfkITlQcSji+wKBgQDJd5PY5rQw9828wjz9cnoZNqwkufMdPcch\\nJe//ShO9OfULpsWy4rfFBu2/tHyCqqaUcvRqscZundv2vjsL3DdYZs1jOY5YGTbw\\n21gMTuG6TdwdEFbcjpuQVtxtKtx8rteGlD0kgkfHBCLLFy25I3Z0IdHYpokwhoV6\\ny9fu4OZYCwKBgCGJy05QqwRYUe4q9oeqXg/xkdr+RaQcU5s38IKChI0w/iUtbCNI\\nc+2Q3bTuesTfL9OjYx2E88nSY/u8clLmd5OOu1TusWnH3eg3NFsHoe1LjV4Y5b8o\\nPqRHSBZr9xpfTczVRbdWdvV/3wxNyUyhxQIAIXH70cMwhB/hTllZjki5AoGBAJzq\\nCblSVt4c4501HBO66eZhcspYGkaYK1RiG/8WTzF+xqEAj9K4SrGmp948UQ3kpEHT\\nFxNSy160jPItUAf4yRKNX0L9PSTY3rXR4U4U0jMtGVd5tm2qlPdi7+QHEhv6gdtL\\n8iNRZV5YllDgCLi+KjBhdvPrIcctvyDZS6G+gSnpAoGBANz5u8zMc6rA4y/r6jQE\\nFtc5uTJOVXLw8TjvP736W/ggNK/G6yZcHF7RinJdPaOMgZXUviGVKAKI77udXoEF\\ntuLSKxq33z7b7FZXQU1/UqDKxTXsA4+X6ngp27xZgNN0kBdIPKubm4sVktnb8OcY\\nopw2kwxa3rd5pTPiNYNwYBt0\\n-----END PRIVATE KEY-----\\n\",\r\n" + 
	  		"  \"client_email\": \"yenihizmet@nesnealgilama.iam.gserviceaccount.com\",\r\n" + 
	  		"  \"client_id\": \"103544450397571893095\",\r\n" + 
	  		"  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\r\n" + 
	  		"  \"token_uri\": \"https://oauth2.googleapis.com/token\",\r\n" + 
	  		"  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\r\n" + 
	  		"  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/yenihizmet%40nesnealgilama.iam.gserviceaccount.com\"\r\n" + 
	  		"}\r\n" + 
	  		"";

	  public String getGservicesConfig() {
		return gservicesConfig;
	}

	public void setGservicesConfig(String gservicesConfig) {
		this.gservicesConfig = gservicesConfig;
	}
	

	@Bean
	  public FirebaseApp provideFirebaseOptions() throws IOException {
	    JSONObject jsonObject = new JSONObject(gservicesConfig);
	    InputStream is = new         ByteArrayInputStream(jsonObject.toString().getBytes());
	    FirebaseOptions options = new FirebaseOptions.Builder()
	        .setCredentials(GoogleCredentials.fromStream((is)))
	        .build();

	    return FirebaseApp.initializeApp(options);
	    
	  }
}
*/

