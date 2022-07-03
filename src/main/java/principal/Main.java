package principal;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import models.Usuario;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        //creando usuario.
        Usuario admin = new Usuario("admin","123adm");

        //Creando la instancia del servidor.
        Javalin app = Javalin.create(config ->{
            config.addStaticFiles(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/public";
                staticFileConfig.location = Location.CLASSPATH;
            });
            config.enableCorsForAllOrigins();

        }).start(7777);

        //si no hay usuario, redirecciona para que se tenga que registrar.
        app.before("/",ctx -> {
            Usuario usuario = ctx.sessionAttribute("usuario");
            if(usuario ==null){
                ctx.render("public/login.html");
            }

        });

        app.get("/",ctx ->{
            Usuario usuario = ctx.sessionAttribute("usuario");
            Map<String,Object> modelo =new HashMap<>();
            modelo.put("usuario",usuario);
            if(usuario != null)
                ctx.render("public/inicio.html",modelo);

        });


        app.post("/login", ctx -> {
            String nombreUser = ctx.formParam("nombreUser");
            String password = ctx.formParam("password");

            if(admin.getUsuario().equalsIgnoreCase(nombreUser) && admin.getContrasena().equalsIgnoreCase(password)){

                ctx.sessionAttribute("usuario", admin);
                ctx.redirect("/");

            }
            else {

                ctx.result("Lo lamentamos, pero el usuario o password se encuentra incorrecto D:");

            }

        });






    }


}