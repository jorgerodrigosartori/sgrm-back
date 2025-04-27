package br.com.sartori.sgrm.util;

import org.apache.commons.codec.language.Soundex;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.apache.commons.text.similarity.LevenshteinDistance;

public class UtilRadical {

	@SuppressWarnings("deprecation")
	public static Integer getPercentualRelevancia(String string1, String string2){

		int distance = StringUtils.getLevenshteinDistance(string1, string2);
		//System.out.println("Distância de Levenshtein: " + distance);

		double similarity = 1.0 - (double) distance / Math.max(string1.length(), string2.length());
		
		return Double.valueOf(similarity * 100).intValue();
		
	}
	
	
	// Mede o número mínimo de operações (inserção, remoção ou substituição) para transformar uma palavra em outra.
	public static void comparador1() {
		
        LevenshteinDistance levenshtein = new LevenshteinDistance();
        String palavra1 = "casa";
        String palavra2 = "caso";

        int distancia = levenshtein.apply(palavra1, palavra2);
        System.out.println("Distância de Levenshtein: " + distancia);
    }
	
	//Mede a similaridade com base nos conjuntos de caracteres compartilhados
	public static void comparador2() {
        JaccardSimilarity jaccard = new JaccardSimilarity();
        String palavra1 = "casa";
        String palavra2 = "caso";

        double similaridade = jaccard.apply(palavra1, palavra2);
        System.out.println("Similaridade de Jaccard: " + similaridade);
    }
	
	//Usa um código fonético para palavras semelhantes na pronúncia
	public static void comparador3() {
        Soundex soundex = new Soundex();
        String palavra1 = "casa";
        String palavra2 = "kaza";

        String codigo1 = soundex.encode(palavra1);
        String codigo2 = soundex.encode(palavra2);

        System.out.println("Código Soundex de 'casa': " + codigo1);
        System.out.println("Código Soundex de 'kaza': " + codigo2);

        if (codigo1.equals(codigo2)) {
            System.out.println("Palavras similares foneticamente.");
        } else {
            System.out.println("Palavras diferentes foneticamente.");
        }
    }
}
