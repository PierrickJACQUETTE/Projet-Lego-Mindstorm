import java.util.HashMap;

public class IdentColor {

    public static HashMap<Integer, String> colorPalette = new HashMap<Integer, String>();

    //CODAGE EN BGR
    public static void init(){
        colorPalette.put(111,"NOIR");
        colorPalette.put(211,"BLEU FONCE");
        colorPalette.put(311,"BLEU");
        colorPalette.put(121,"VERT FONCE");
        colorPalette.put(221,"CYAN FONCE");
        colorPalette.put(321,"AZUR");
        colorPalette.put(131,"VERT");
        colorPalette.put(231,"TURQUOISE");
        colorPalette.put(331,"CYAN");
        colorPalette.put(112,"ROUGE FONCE");
        colorPalette.put(212,"MAGENTA FONCE");
        colorPalette.put(312,"VIOLET");
        colorPalette.put(122,"JAUNE FONCE");
        colorPalette.put(222,"GRIS");
        colorPalette.put(322,"BLEU CLAIR");
        colorPalette.put(132,"VERT LIME");
        colorPalette.put(232,"VERT CLAIR");
        colorPalette.put(332,"CYAN CLAIR");
        colorPalette.put(113,"ROUGE");
        colorPalette.put(213,"FUSHIA");
        colorPalette.put(313,"MAGENTA");
        colorPalette.put(123,"ORANGE");
        colorPalette.put(223,"ROUGE CLAIR");
        colorPalette.put(323,"MAGENTA CLAIR");
        colorPalette.put(133,"JAUNE");
        colorPalette.put(233,"JAUNE CLAIR");
        colorPalette.put(333,"BLANC");
    }

    public static int colorToCode(int c, int d) throws Exception {
        int r;
        if(c >= 0 && c < 85){
            r = 1*d;
        } else if(c >= 85 && c < 170){
            r = 2*d;
        } else if(c >= 170 && c <= 255){
            r = 3*d;
        } else {
            throw new Exception();
        }
        return r;
    }

    public static String ident(int[] tab){
        int tmp = 0;
        for(int i = 0; i < 3; i++){
            try{
                tmp += colorToCode(tab[i],(int)Math.pow(10,i));
            } catch(Exception e){
                System.out.println("Je n'ai pas cette couleur.");
            }
        }
        System.out.println(tmp);
        return colorPalette.get(tmp);
    }

    public static void main(String[] args) {
        init();
        int[] t = {255,255,255};
        System.out.println(ident(t));
    }
}
