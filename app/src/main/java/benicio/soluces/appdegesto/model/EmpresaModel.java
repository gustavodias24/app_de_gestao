package benicio.soluces.appdegesto.model;

public class EmpresaModel {
    String login, senha;

    public EmpresaModel() {
    }

    public EmpresaModel(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
