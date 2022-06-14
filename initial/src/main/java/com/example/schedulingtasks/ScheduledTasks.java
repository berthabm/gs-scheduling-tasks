package com.example.schedulingtasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import ohSolutions.ohJpo.dao.Jpo;
import ohSolutions.ohJpo.dao.JpoClass;
import ohSolutions.ohJpo.dao.Tabla;

@Component
public class ScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	//@Scheduled(fixedRate = 5000)
	public void reportCurrentTime() {
		log.info("The time is now{}", dateFormat.format(new Date()));
	}

	//@Scheduled(fixedRate = 10000)
	public void testserviciorest() throws Exception {
		
		String mybody = "{\r\n"
				+ "  \"DMG\": {\r\n"
				+ "    \"F\": {\r\n"
				+ "      \"pf_user_id\": 1,\r\n"
				+ "      \"nombreRecepcion\": \"ejemplo\",\r\n"
				+ "      \"fechaNacimientoRecepcion\": \"10/06/2022\",\r\n"
				+ "      \"asistenciaRecepcion\": 1\r\n"
				+ "    }\r\n"
				+ "  }\r\n"
				+ "}";
			
		HttpResponse<String> responseT = 
                Unirest.post("http://localhost:8462/ovnDMG/module/dmg/DMGReceptionServiceImp/dmgreceptionRegister").body(mybody).asString();
		
		System.out.println(responseT.getBody());
        //JSONObject jsonObject = new JSONObject();
		
	}
	
	@Scheduled(fixedRate = 10000)
	public void mostrarTabla() throws Exception {
		Map<String, String> config = new HashMap<String, String>();

        config.put("type", "POSTGRESQL");
        config.put("url", "localhost");
        config.put("db", "berthaPruebas");
        config.put("username", "postgres");
        config.put("password", "123456789");

        Jpo newJpo = new Jpo(config);
        
        
        //Object resultado = newJpo.tabla("VACTV_EIR").donde("EIR_C_NUMERO = '0000757933'").seleccionar("*");
        //Object resultado = newJpo.tabla("pruebas").donde("edad = 19").seleccionar("nombre, edad");
        
        // [{"nombre":"Bertha","edad":19}]
        Tabla aTotalizadorH = newJpo.tabla("\"aTotalizadorH\"");
        Tabla aVenta = newJpo.tabla("\"aVenta\"");
        
       //tPrueba.donde("asistencia = 0"); 
        
       // List<Object> resultadoId = (List<Object>)tPrueba.donde("asistencia = 0").seleccionar("id");

        List<Object> resultadoaTotalizadorH = (List<Object>) aTotalizadorH.seleccionar("*");
        List<Object> resultadoaVenta = (List<Object>) aTotalizadorH.seleccionar("*");
        
        
        for(int i = 0; i < resultadoaTotalizadorH.size(); i++) {
        	Map<String, Object> item = (Map<String, Object>) resultadoaTotalizadorH.get(i);
        	//Map<String, Object> item2 = (Map<String, Object>) resultadoId.get(i);
        	
        	System.out.println(item);
        	
        	System.out.println(item.get("thID"));
        	
        	System.out.println(item.get("thHora"));
        	System.out.println(item.get("thCara"));
        	System.out.println(item.get("thManguera"));
        	System.out.println(item.get("thTipo"));
        	System.out.println(item.get("thImporte"));
        	System.out.println(item.get("thVolumen"));
        	
        	/*Date fecha = (Date) item.get("fechanacimiento");
        	
        	SimpleDateFormat SDFormat = new SimpleDateFormat("dd/MM/yyyy");
        	String fechaCast = SDFormat.format(fecha);
        	
        	String mybody = "{\r\n"
    				+ "  \"DMG\": {\r\n"
    				+ "    \"F\": {\r\n"
    				+ "      \"pf_user_id\": "+item.get("id")+",\r\n"
    				+ "      \"nombreRecepcion\": \""+item.get("nombre")+"\",\r\n"
    				+ "      \"fechaNacimientoRecepcion\": \""+fechaCast+"\",\r\n"
    				+ "      \"asistenciaRecepcion\": 1\r\n"
    				+ "    }\r\n"
    				+ "  }\r\n"
    				+ "}";
    			
    		HttpResponse<String> responseT = 
                    Unirest.post("http://localhost:8462/ovnDMG/module/dmg/DMGReceptionServiceImp/dmgreceptionRegister").body(mybody).asString();
    		
    		System.out.println(responseT.getBody());
        	
        	
        	
            Tabla tPruebaEdit = newJpo.tabla("pruebas");
            
             tPruebaEdit.donde("id ="+item.get("id"));
            
             tPruebaEdit.setData("asistencia", "1");
            
             tPruebaEdit.editar(); // UPDATE pruebas SET estado = 0 where pruea_id = 1*/
            
        	
        	
        }
        
        newJpo.commit();
        
        System.out.println("Resultado de prueba");
        System.out.println((new Gson()).toJson(resultadoaTotalizadorH));

		System.out.println("Hola mundo");
	}
}
