package com.example.schedulingtasks;

import static org.mockito.ArgumentMatchers.any;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        List<Object> resultadoaVenta = (List<Object>) aVenta.ordenadoPor("\"veID\"").seleccionar("*");
        List<Object> ejemploLista = new ArrayList<Object>();
        
        for(int i = 0; i < resultadoaVenta.size(); i++) {
        	Map<String, Object> itemVenta = (Map<String, Object>) resultadoaVenta.get(i);
        	
        	Object horaVe = itemVenta.get("veHoraPC");
        	Object caraVe = itemVenta.get("veCara");
        	Object estadoVe = itemVenta.get("veEstado");
        	
        	for(int j = 0; j < resultadoaTotalizadorH.size(); j++) {
            	Map<String, Object> itemTotalizador = (Map<String, Object>) resultadoaTotalizadorH.get(j);
            	
            	Object horaTh = itemTotalizador.get("thHora");
            	Object caraTh = itemTotalizador.get("thCara");
            	Object importeTh = itemTotalizador.get("thImporte");
            	Object volumenTh = itemTotalizador.get("thVolumen");
            	
            	if(horaTh.equals(horaVe)&& caraVe.equals(caraTh)&&estadoVe.equals(0)) {
            		aVenta.donde("\"veEstado\"=0");
            		
            		ejemploLista.add(importeTh);
            		ejemploLista.add(volumenTh);
            	} else {
            		
            	}
        	}
        	
        }
        

        List<Object> resultadoQuery = (List<Object>)aVenta.seleccionar("*");
        for(int i = 0; i < resultadoQuery.size(); i++) {
        	Map<String, Object> itemQuery = (Map<String, Object>) resultadoQuery.get(i);
        	Object importeTh = ejemploLista.get(i);
        	Object volumenTh = ejemploLista.get(i + 1);
        
        	SimpleDateFormat SDFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        	
        	System.out.print(itemQuery.get("veID") + " ");
        	
            Date fecha = (Date) itemQuery.get("veHoraPC");      	
        	String veHoraPC = SDFormat.format(fecha);
        	
        	System.out.print(veHoraPC + " ");
        	
        	fecha = (Date) itemQuery.get("veHoraCS"); 
        	String veHoraCS = SDFormat.format(fecha);
        	
        	System.out.print(veHoraCS + " ");
        	System.out.print(itemQuery.get("veCara")+ " ");
        	System.out.print(itemQuery.get("veManguera")+ " ");
        	System.out.print(itemQuery.get("vePago")+ " ");
        	System.out.print(itemQuery.get("veProducto")+ " "); 
        	System.out.print(itemQuery.get("vePrecio")+ " ");
        	System.out.print(itemQuery.get("vePago")+ " ");
        	System.out.print(itemQuery.get("veVolumen")+ " "); 
        	System.out.print(itemQuery.get("veImporte")+ " ");
        	System.out.print(itemQuery.get("veEstado")+ " "); 
        	
        	System.out.println(importeTh + " " + volumenTh);
        	
        	
        	
        	/*String mybody = "{\r\n"
    				+ "  \"DMG\": {\r\n"
    				+ "    \"F\": {\r\n"
    				+ "      \"pf_user_id\": 1,\r\n"
    				+ "      \"store_alias\": \"ovn_str_sac123\",\r\n"
    				+ "      \"cat_type_pump_alias\": \"dmg_pumptype_r1\",\r\n"
    				+ "      \"origin_id\": 4,\r\n"
    				+ "      \"origin_datetime_pc\": \"08/05/2007 12:39:29\",\r\n"
    				+ "      \"origin_datetime_cs\": \"08/05/2007 12:35:29\",\r\n"
    				+ "      \"origin_side\": 165,\r\n"
    				+ "      \"origin_hose\": 998,\r\n"
    				+ "      \"origin_payment\": 567,\r\n"
    				+ "      \"origin_price\": 165.087,\r\n"
    				+ "      \"origin_volume\": 55.087,\r\n"
    				+ "      \"origin_product\": \"hgk\",\r\n" 
    				+ "      \"origin_amount\": 3456.987,\r\n"
    				+ "      \"origin_tot_volume\": 8833.776,\r\n"
    				+ "      \"origin_tot_amount\": 8661.020,\r\n"
    				+ "      \"status\": 1\r\n"
    				+ "    }\r\n"
    				+ "  }\r\n"
    				+ "}";
    			
    		HttpResponse<String> responseT = 
                    Unirest.post("http://localhost:8462/ovnDMG/module/dmg/DMGGasoline_PumpServiceImp/dmggasolinePumpRegister").body(mybody).asString();
    		
    		System.out.println(responseT.getBody());*/
        	
        	
        	
            /*Tabla tPruebaEdit = newJpo.tabla("pruebas");
            
             tPruebaEdit.donde("id ="+item.get("id"));
            
             tPruebaEdit.setData("asistencia", "1");
            
             tPruebaEdit.editar(); // UPDATE pruebas SET estado = 0 where pruea_id = 1*/
            
        }
        	
        
        
        newJpo.commit();
             
        System.out.println((new Gson()).toJson(resultadoaTotalizadorH));
        System.out.println((new Gson()).toJson(resultadoQuery));
		System.out.println("Hola mundo");
	}
}
