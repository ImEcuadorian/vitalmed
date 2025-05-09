package io.github.imecuadorian.vitalmed.model.country;

import java.util.*;
import java.util.stream.*;

public enum Province implements Cantons {
   
   AZUAY(1, "Azuay") {
      @Override
      public List<String> getListCantons() {
         return Stream.of("Sevilla de Oro", "Paute", "Guachapala", "El Pan", "Gualaceo",
                          "Chordeleg", "Sígsig", "Cuenca", "Santa Isabel", "Pucará",
                          "Camilo Ponce Enríquez", "San Fernando", "Girón", "Nabón",
                          "Oña").sorted().toList();
      }
   }, BOLIVAR(2, "Bolivar") {
      @Override
      public List<String> getListCantons() {
         return Stream.of("Guaranda", "Las Naves", "Echeandía", "Caluma", "Chimbo",
                          "San Miguel", "Chillanes").sorted().toList();
      }
   }, CANAR(3, "Cañar") {
      @Override
      public List<String> getListCantons() {
         return Stream.of("La Troncal", "Cañar", "Suscal", "El Tambo", "Azogues",
                          "Biblián", "Déleg").sorted().toList();
      }
   }, CARCHI(4, "Carchi") {
      @Override
      public List<String> getListCantons() {
         return Stream.of("Bolívar", "Espejo", "Mira", "Montúfar", "San Pedro de Huaca",
                          "Tulcán").sorted().toList();
      }
   }, CHIMBORAZO(5, "Chimborazo") {
      @Override
      public List<String> getListCantons() {
         return Stream.of("Guano", "Penipe", "Riobamba", "Colta", "Chambo", "Pallatanga",
                          "Guamote", "Alausí", "Cumandá", "Chunchi").sorted().toList();
      }
   }, COTOPAXI(6, "Cotopaxi") {
      @Override
      public List<String> getListCantons() {
         return Stream.of("Sigchos", "La Maná", "Latacunga", "Saquisilí", "Pujilí",
                          "Pangua", "Salcedo").sorted().toList();
      }
   }, EL_ORO(7, "El Oro") {
      @Override
      public List<String> getListCantons() {
         return Stream
            .of("El Guabo", "Machala", "Pasaje", "Chilla", "Zaruma", "Santa Rosa",
                "Atahualpa", "Arenillas", "Huaquillas", "Las Lajas", "Marcabelí",
                "Balsas", "Piñas", "Portovelo")
            .sorted()
            .toList();
      }
   }, ESMERALDAS(8, "Esmeraldas") {
      @Override
      public List<String> getListCantons() {
         return Stream.of("San Lorenzo", "Eloy Alfaro", "Rioverde", "Esmeraldas",
                          "Muisne", "Atacames", "Quinindé").sorted().toList();
      }
   }, GALAPAGOS(9,"Galapágos") {
      @Override
      public List<String> getListCantons() {
         return Stream.of("Isabela", "San Cristóbal", "Santa Cruz").sorted().toList();
      }
   }, GUAYAS(10, "Guayas") {
      @Override
      public List<String> getListCantons() {
         return Stream.of("Guayaquil", "Alfredo Baquerizo Moreno", "Balao", "Balzar",
                          "Colimes", "Coronel Marcelino Maridueña", "Daule", "Durán",
                          "El Empalme", "El Triunfo", "General Antonio Elizalde",
                          "Isidro Ayora", "Lomas de Sargentillo", "Milagro", "Naranjal",
                          "Naranjito", "Nobol", "Palestina", "Pedro Carbo", "Playas",
                          "Salitre", "Samborondón", "Santa Lucía", "Simón Bolívar",
                          "Yaguachi").sorted().toList();
      }
   }, IMBABURA(11, "Imbabura") {
      @Override
      public List<String> getListCantons() {
         return Stream.of("Antonio Ante", "Cotacachi", "Ibarra", "Otavalo", "Pimampiro",
                          "San Miguel de Urcuquí").sorted().toList();
      }
   }, LOJA(12, "Loja") {
      @Override
      public List<String> getListCantons() {
         return Stream
            .of("Calvas", "Catamayo", "Celica", "Chaguarpamba", "Espíndola", "Gonzanamá",
                "Loja", "Macará", "Olmedo", "Paltas", "Pindal", "Puyango", "Quilanga",
                "Saraguro", "Sozoranga", "Zapotillo")
            .sorted()
            .toList();
      }
   }, LOS_RIOS(13, "Los Ríos") {
      @Override
      public List<String> getListCantons() {
         return Stream.of("Baba", "Babahoyo", "Buena Fe", "Mocache", "Montalvo",
                          "Palenque", "Puebloviejo", "Quevedo", "Quinsaloma", "Urdaneta",
                          "Valencia", "Ventanas", "Vinces").sorted().toList();
      }
   }, MANABI(14, "Manabí") {
      @Override
      public List<String> getListCantons() {
         return Stream.of("Bolívar", "Chone", "El Carmen", "Flavio Alfaro", "Jama",
                          "Jaramijó", "Jipijapa", "Junín", "Manta", "Montecristi",
                          "Olmedo", "Paján", "Pedernales", "Pichincha", "Portoviejo",
                          "Puerto López", "Rocafuerte", "San Vicente", "Santa Ana",
                          "Sucre", "Tosagua", "Veinticuatro de Mayo").sorted().toList();
      }
   }, MORONA_SANTIAGO(15, "Morona Santiago") {
      @Override
      public List<String> getListCantons() {
         return Stream.of("Gualaquiza", "Huamboya", "Limón Indanza", "Logroño", "Morona",
                          "Pablo Sexto", "Palora", "San Juan Bosco", "Santiago", "Sucúa",
                          "Taisha", "Tiwintza").sorted().toList();
      }
   }, NAPO(16, "Napo") {
      @Override
      public List<String> getListCantons() {
         return Stream.of("Archidona", "Carlos Julio Arosemena Tola", "El Chaco",
                          "Quijos", "Tena").sorted().toList();
      }
   }, ORELLANA(17, "Orellana") {
      @Override
      public List<String> getListCantons() {
         return Stream
            .of("Aguarico", "Orellana", "La Joya de los Sachas", "Loreto")
            .sorted()
            .toList();
      }
   }, PASTAZA(18, "Pastaza") {
      @Override
      public List<String> getListCantons() {
         return Stream.of("Arajuno", "Mera", "Pastaza", "Santa Clara").sorted().toList();
      }
   }, PICHINCHA(19, "Pichincha") {
      @Override
      public List<String> getListCantons() {
         return Stream.of("Cayambe", "Mejía", "Pedro Moncayo", "Pedro Vicente Maldonado",
                          "Puerto Quito", "Distrito Metropolitano de Quito", "Rumiñahui",
                          "San Miguel de Los Bancos").sorted().toList();
      }
   }, SANTA_ELENA(20, "Santa Elena") {
      @Override
      public List<String> getListCantons() {
         return Stream.of("La Libertad", "Salinas", "Santa Elena").sorted().toList();
      }
   }, SANTO_DOMINGO_DE_LOS_TSACHILAS(21,"Santo Domingo de los Tsáchilas") {
      @Override
      public List<String> getListCantons() {
         return Stream.of("La Concordia", "Santo Domingo").sorted().toList();
      }
   }, SUCUMBIOS(22,"Sucumbios") {
      @Override
      public List<String> getListCantons() {
         return Stream.of("Cascales", "Cuyabeno", "Gonzalo Pizarro", "Lago Agrio",
                          "Putumayo", "Shushufindi", "Sucumbíos").sorted().toList();
      }
   }, TUNGURAHUA(23, "Tungurahua") {
      @Override
      public List<String> getListCantons() {
         return Stream.of("Ambato", "Baños de Agua Santa", "Cevallos", "Mocha", "Patate",
                          "Pelileo", "Píllaro", "Quero", "Tisaleo").sorted().toList();
      }
   }, ZAMORA_CHINCHIPE(24, "Zamora Chinchipe") {
      @Override
      public List<String> getListCantons() {
         return Stream
            .of("Centinela del Cóndor", "Chinchipe", "El Pangui", "Nangaritza", "Palanda",
                "Paquisha", "Yacuambi", "Yantzaza", "Zamora")
            .sorted()
            .toList();
      }
   };
   
   private final int id;
   private final String name;
   
   Province(int id, String name) {
      this.id = id;
      this.name = name;
   }
   
   public int getId() {
      return id;
   }
   
   public String getName() {
      return name;
   }


   @Override
   public String toString() {
        return getName();
   }
}
