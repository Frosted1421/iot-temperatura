class teste{
	public static void main(String[]args){
	        String novaMedicao="40TE80UM";
		if (novaMedicao.length() == 8) {
              	  String novaTemp = novaMedicao.substring(0, 2);
                  String flagTemp = novaMedicao.substring(2, 4);
                  String novaUmidade = novaMedicao.substring(4, 6);
                  String flagUmidade = novaMedicao.substring(6, 8);

                  if (flagTemp.equals("TE") && flagUmidade.equals("UM")) {
                      //addTempUmi(novaTemp, novaUmidade);
		      System.out.println("funcionou");
                  } else {
                      System.out.println("Flags inválidas");
                  }
                  } else {
                      System.out.println("Tamanho inválido");
                    } 	
	}
}
