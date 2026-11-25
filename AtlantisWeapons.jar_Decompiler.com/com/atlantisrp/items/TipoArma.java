package com.atlantisrp.items;

public enum TipoArma {
   GLOCK("glock", "Glock", 18, 394, 17, Munizione.NOVE_MM, (double)4.5F, false, 667L, (double)40.0F),
   FS92("fs92", "Beretta 92FS", 8, 394, 15, Munizione.NOVE_MM, (double)6.0F, false, 667L, (double)40.0F),
   PX4("px4", "Beretta PX4", 6, 394, 17, Munizione.NOVE_MM, (double)7.0F, false, 667L, (double)40.0F),
   MP5("mp5", "MP5", 17, 395, 30, Munizione.NOVE_MM, 5.2, true, 500L, (double)40.0F),
   PM12("pm12", "Beretta PM-12", 4, 395, 30, Munizione.NOVE_MM, (double)6.0F, true, 600L, (double)40.0F),
   PMX("pmx", "Beretta PMX", 5, 395, 30, Munizione.NOVE_MM, (double)5.0F, true, 500L, (double)40.0F),
   M4A1("m4a1", "Colt M4A1s", 9, 395, 30, Munizione.NATO_556, (double)7.0F, true, 320L, (double)40.0F),
   AUG("aug", "Steyr AUG", 13, 395, 30, Munizione.NATO_556, (double)8.0F, false, 500L, (double)40.0F),
   SC7090("sc7090", "Beretta SC70/90", 7, 395, 30, Munizione.NATO_556, (double)9.0F, false, 500L, (double)40.0F),
   HK416("hk416", "HK-416", 14, 395, 30, Munizione.NATO_556, (double)8.5F, true, 500L, (double)40.0F),
   ARX200("arx200", "ARX 200", 1, 395, 20, Munizione.NATO_762, (double)11.0F, false, 500L, (double)40.0F),
   SPAS15("spas15", "Franchi SPAS-15", 10, 395, 8, Munizione.POMPA, (double)16.0F, false, 800L, (double)40.0F),
   BARRETT("barrett", "Barrett M82", 2, 395, 10, Munizione.CECCHINO, (double)40.0F, false, 1500L, (double)70.0F),
   SAKO("sako", "Sako TRG-42", 12, 395, 5, Munizione.CECCHINO, (double)40.0F, false, 1800L, (double)70.0F);

   private final String id;
   private final String nome;
   private final int cmdArma;
   private final int cmdCaricatore;
   private final int capacita;
   private final Munizione munizione;
   private final double dannoTesta;
   private final boolean automatica;
   private final long cooldownMs;
   private final double gittata;

   private TipoArma(String id, String nome, int cmdArma, int cmdCaricatore, int capacita, Munizione munizione, double dannoTesta, boolean automatica, long cooldownMs, double gittata) {
      this.id = id;
      this.nome = nome;
      this.cmdArma = cmdArma;
      this.cmdCaricatore = cmdCaricatore;
      this.capacita = capacita;
      this.munizione = munizione;
      this.dannoTesta = dannoTesta;
      this.automatica = automatica;
      this.cooldownMs = cooldownMs;
      this.gittata = gittata;
   }

   public String getId() {
      return this.id;
   }

   public String getNome() {
      return this.nome;
   }

   public int getCmdArma() {
      return this.cmdArma;
   }

   public int getCmdCaricatore() {
      return this.cmdCaricatore;
   }

   public int getCapacita() {
      return this.capacita;
   }

   public Munizione getMunizione() {
      return this.munizione;
   }

   public int colpiPerUccidere(boolean testa, int livello) {
      int[] corpo;
      int[] head;
      switch (this.ordinal()) {
         case 0:
            corpo = new int[]{9, 11, 15};
            head = new int[]{7, 9, 12};
            break;
         case 1:
            corpo = new int[]{8, 10, 12};
            head = new int[]{6, 8, 9};
            break;
         case 2:
            corpo = new int[]{6, 8, 10};
            head = new int[]{4, 6, 8};
            break;
         default:
            return -1;
      }

      return (testa ? head : corpo)[livello];
   }

   public double getDannoTesta() {
      int c = this.colpiPerUccidere(true, 0);
      return c > 0 ? (double)20.0F / (double)c : this.dannoTesta;
   }

   public double getDannoCorpo() {
      int c = this.colpiPerUccidere(false, 0);
      return c > 0 ? (double)20.0F / (double)c : Math.max((double)1.0F, this.dannoTesta - (double)2.0F);
   }

   public boolean isAutomatica() {
      return this.automatica;
   }

   public long getCooldownMs() {
      return this.cooldownMs;
   }

   public double getGittata() {
      return this.gittata;
   }

   public boolean isPistola() {
      return this == GLOCK || this == FS92 || this == PX4;
   }

   public boolean isSuonoAlto() {
      return this == BARRETT || this == SAKO || this == SPAS15;
   }

   public String getSuonoSparo() {
      if (this.isPistola()) {
         return "weapons.bulletlow";
      } else {
         return this.isSuonoAlto() ? "weapons.bullethigh" : "weapons.bulletmedium";
      }
   }

   public static TipoArma da(String id) {
      if (id == null) {
         return null;
      } else {
         for(TipoArma t : values()) {
            if (t.id.equals(id)) {
               return t;
            }
         }

         return null;
      }
   }

   // $FF: synthetic method
   private static TipoArma[] $values() {
      return new TipoArma[]{GLOCK, FS92, PX4, MP5, PM12, PMX, M4A1, AUG, SC7090, HK416, ARX200, SPAS15, BARRETT, SAKO};
   }
}
