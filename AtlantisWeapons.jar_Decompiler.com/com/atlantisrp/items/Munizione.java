package com.atlantisrp.items;

public enum Munizione {
   NOVE_MM("munizione_9mm", "Munizioni 9mm", 91),
   CECCHINO("munizione_cecchino", "Munizioni Cecchino", 92),
   NATO_556("munizione_556", "Munizioni 5.56 NATO", 94),
   NATO_762("munizione_762", "Munizioni 7.62x51", 95),
   POMPA("munizione_pompa", "Munizioni Pompa", 96);

   private final String id;
   private final String nome;
   private final int cmd;

   private Munizione(String id, String nome, int cmd) {
      this.id = id;
      this.nome = nome;
      this.cmd = cmd;
   }

   public String getId() {
      return this.id;
   }

   public String getNome() {
      return this.nome;
   }

   public int getCmd() {
      return this.cmd;
   }

   // $FF: synthetic method
   private static Munizione[] $values() {
      return new Munizione[]{NOVE_MM, CECCHINO, NATO_556, NATO_762, POMPA};
   }
}
