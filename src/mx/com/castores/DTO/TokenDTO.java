package mx.com.castores.dto;

public class TokenDTO {
	private String token;
        private String urlServidor;
	private String urlToken;
	private String clientID;
	private String clientSecret;
	private String userName;
	private String password;
        
        
        
    public String getUrlServidor() {
        return urlServidor;
    }

    public void setUrlServidor(String urlServidor) {
        this.urlServidor = urlServidor;
    }
    
    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrlToken() {
        return urlToken;
    }

    public void setUrlToken(String urlToken) {
        this.urlToken = urlToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
	
	public TokenDTO() {
		this.token = "";
		this.urlToken = "";
		this.clientID = "";
		this.clientSecret = "";
		this.userName = "";
		this.password = "";
	}
}
