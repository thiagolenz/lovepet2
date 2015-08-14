package lovepetapp.com.lovepet.bean;

/**
 * Created by macbookpro on 14/08/15.
 */
public class Usuario {
    private String nome;
    private String email;
    private String senha;
    private String aniversario;

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getAniversario() {
        return aniversario;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setAniversario(String aniversario) {
        this.aniversario = aniversario;
    }
}
