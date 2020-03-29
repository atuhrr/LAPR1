
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class LAPR1 {
	
	static Scanner teclado = new Scanner(System.in);
    static String FILE = "DAYTON.csv";
    static String resolucao;
    static String modelo;
    static String ordenacao;
    static Integer n=null;
    static Integer alfa=null;
    static final int MAX_LINHAS = 100000;
    static final int MAX_COLUNAS = 7;
    static final int ANO = 0;
    static final int MES = 1;
    static final int DIA = 2;
    static final int HORAS = 3;
    static final int MINUTOS = 4;
    static final int SEGUNDOS = 5;
    static final int CONSUMO = 6;
    static final int MADRUGADA = 0;
    static final int MANHA = 1;
    static final int TARDE = 2;
    static final int NOITE = 3;
    public static int[][] dados = new int[MAX_LINHAS][MAX_COLUNAS];
    public static int[] serie = new int[MAX_LINHAS];
    public static float[] media = new float[MAX_LINHAS];
    public static int linhas = 0;
  //Como a análise é dinâmica, é necessário o uso de arraylist pelo seu tamanho dinâmico
    public static ArrayList<Double> dataset = new ArrayList<Double>();
    public static ArrayList<Double> estimativas = new ArrayList<Double>();
    ///////////////////Esse array é apenas para testar a função///////////////////
	/////No programa ele deve ser formado pelos dados da coluna analizada da tabela/////
	public static double[] input = {1,5,6,1,3,2,5,9,7,3,10,15,21,14,23,11,9,20};
	//////////////////////////////////////////////////////////////////////////////
	public static int periodo;//Na fórmula esse é o n
    public static double soma = 0;
    
    public static void main(String[] args) throws FileNotFoundException 
    {        
    	//Essa parte comentada não funciona ainda
        /*validacao_argumentos(args);
        lerFicheiro();
        analisarSeriesTemporais();*/
    	setPeriodo();
    	selecionarMetodo();
    	System.out.println("Dados: " + printArray(input));
    	System.out.println("Estimativas: " + estimativas);
    	System.out.println("Erro médio absoluto: " + calcularEMA());
    }
    
    public static void setPeriodo()
    {
    	System.out.println("Insira o periodo da análise: ");
    	periodo = teclado.nextInt();
    }
    
    public static double getUltimaEstimativa(int index)
    {
    	return estimativas.get(estimativas.size()-1);
    }
    
    public static String printArray(double[] arr)
    {
    	//Construido por concatenação pois não sei se pode usar StringBuilder
    	//mesmo sendo bem mais rápido
    	String array = "";
    	for (int i = 0; i < arr.length; i++)
    	{
			array += arr[i] + " ";
		}
    	return array.trim();
    }
    
    //Função para calcular o erro médio absoluto
    public static double calcularEMA()
	{
		double soma = 0;
		for(int i = 0; i<dataset.size(); i++)
		{	//Essa é a fórmula que se encontra no pdf
			soma+=Math.abs(estimativas.get(i) - dataset.get(i));///Somatório de |yi -xi|
		}
		return soma/periodo;
	}

    
    public static void selecionarMetodo()
    {
    	boolean metodoOK = false;
    	System.out.println("Selecione o método para análise: ");
    	System.out.println("1: Média móvel simples");
    	System.out.println("2 Média móvel exponencialmente pesada");
    	do {//Enquanto o usuário não selecionar um méodo válido o sistema vai
    		//continuar pedindo
	    	switch(teclado.nextInt())
	    	{
	    	case 1:
	    		//Zerando os arraylists caso essa função ja tenha sido chamada
	    		//na mesma execução. Se isso não for acontecer pode remover essas
	    		//próximas duas linhas
	    		dataset = new ArrayList<Double>();
	    		estimativas = new ArrayList<Double>();
	    		for(double x : input)
	    	    {
	    	    	soma+=x;
	    	    	dataset.add(x);
	    	    	//Atualizar tamanho da fila para que
	    	    	//o tamanho do dataset seja igual ao
	    	    	//periodo
	    	    	if(dataset.size()>periodo)
	    	    	{
	    	    		soma-=dataset.remove(0);
	    	    	}
	    	    	estimativas.add(soma/periodo);
	    	    }
	    		metodoOK = true;
	    		break;
	    	case 2:
	    		double y = 0;
	    		double alfa = (double)2/(periodo + 1);
	    		double yAnterior;
	    		//Zerando os arraylists caso essa função ja tenha sido chamada
	    		//na mesma execução. Se isso não for acontecer pode remover essas
	    		//próximas duas linhas
	    		dataset = new ArrayList<Double>();
	    		estimativas = new ArrayList<Double>();
	    		for(double x : input)
	    		{
	    			dataset.add(x);
	    			if(dataset.size()>periodo)
	    			{
	    				dataset.remove(0);
	    			}
	    			yAnterior = y;
	    			y = alfa*x+(1-alfa)*yAnterior;
	    			estimativas.add(y);
	    		}
	    		metodoOK = true;
	    		break;
	    	default:
	   			break;
	    	}
	    }while(!metodoOK);
    }
    
    public static void lerFicheiro() throws FileNotFoundException 
    {
        try (Scanner in = new Scanner(new File(FILE))) 
        {
            in.nextLine();
            while (in.hasNextLine() && linhas < MAX_LINHAS) {
                String linha = in.nextLine();
                
                //muda os caracteres ",";"-";" " para ":"
                linha=linha.replace(",", ":");
                linha=linha.replace("-", ":");
                linha=linha.replace(" ", ":");
                
                //divide a string em várias strings, separadas por ":"
                String[] itens = linha.split(":");
                for (int w = 0; w < MAX_COLUNAS; w++) {
                    dados[linhas][w]=Integer.parseInt(itens[w]);
                }
                linhas++;
            }
        }
        catch (Exception e)
        {
            System.out.println("Não existe o ficheiro.");
        }
    }
    public static void verif_nome(String a)
    {
        if (a.equals("-name"))
        {
            FILE=a;
        }
        else
        {
            System.out.println(a+": Opção inválida");
        }
    }
    
    public static void verif_resolucao(String a)
    {
        if (resolucao==null)
        {
            switch (a) 
            {
                case "11":
                case "12":
                case "13":
                case "14":
                case "2":
                case "3":
                case "4":
                    resolucao=a;
                default:
                    System.out.println(a+": Resolução inválida. A resolução deverá ser um dos seguintes valores: 11, 12, 13, 14, 2, 3, 4");
                    break;
            }
        }
        else
        {
            System.out.println("Resolução atribuida");
        }
    }
    
    public static void verif_modelo_e_tipordenacao(String a)
    {
        if (modelo==null)
        {
            switch (a)
            {
                case "1":
                case "2":
                    modelo=a;
                    ordenacao=a;
                default:
                    System.out.println(a+": Valor inválido.");
                    break;
            }
        }
        else
        {
            System.out.println("Modelo e ordenação atribuida");
        }
    }
    
    public static void verif_parmodelo(String a)
    {
        if (n==null || alfa==null)
        {
            if (modelo.equals("1")) n=Integer.parseInt(a);
            if (modelo.equals("2")) alfa=Integer.parseInt(a);
        }
        else
        {
            System.out.println("Par modelo já atribuído");
        }
    }
    
    public static void validacao_argumentos(String args[])
    {
        int i;
        switch (args.length)
        {
            case 8:
                for (i=0;i<8;i=i+2)
                {
                    switch(args[i])
                    {
                        case "-name":
                            verif_nome(args[i]);
                            break;
                        case "-resolucao":
                            verif_resolucao(args[i]);
                            break;
                        case "-modelo":
                            verif_modelo_e_tipordenacao(args[i]);
                            break;
                        case "-tipOrdenacao":
                            verif_modelo_e_tipordenacao(args[i]);
                            break;
                        case "-parModelo":
                            verif_parmodelo(args[i]);
                            break;
                        case "-momentoPrevisao":
                            break;
                        default:
                            System.out.println(args[i]+": Opção inválida");
                    }
                }
            case 2:
                if (args[0].equals("-name")) verif_nome(args[1]);
                Scanner scan = new Scanner(System.in);
                System.out.print("Insira a resolução pretendida: ");
                resolucao=scan.nextLine();
                break;
            default:
                System.out.println("Número de argumentos inválido.");
                break;
        }
    }   
    
    public static void analisarSeriesTemporais(){
        switch (resolucao) 
            {
                case "11":
                    analisarSeriesPartes(6, 12);
                    break;
                case "12":
                    analisarSeriesPartes(12, 18);
                    break;
                case "13":
                    analisarSeriesPartes(18, 24);
                    break;
                case "14":
                    analisarSeriesPartes(0, 6);
                    break;
                case "2":
                    analisarSeriesPartes(0, 24);
                    break;
                case "3":
                    
                    break;
                case "4":
                    
                    break;
                    
                default:
            }
    }
    
    public static void analisarSeriesPartes(int min, int max) {
        int i = 0, cont = 0, contMedia = 0; // i -> serve para ver se mudou de dia ou não || cont -> serve para fazer a média || contMedia -> serve para fazer o vetor media[]
        serie[i] = dados[0][CONSUMO];
        for (int linha = 1; linha < linhas; linha++) {
            if (dados[linha][HORAS] >= min && dados[linha][HORAS] < max && dados[linha][DIA] == dados[linha - 1][DIA]) { //procura se a linha a analisar está no intervalo de horas e no mesmo dia
                serie[i] += dados[linha][CONSUMO];
                cont++; //
            } else if (dados[linha][DIA] != dados[linha - 1][DIA]) {
                media[contMedia] = serie[i] / cont;
                System.out.println(media[i]);
                i++; // acrescentamos mais uma séria (ex: mais uma madrugada)
                serie[i] = dados[linha][CONSUMO];
                contMedia++; // é o número de médias
                cont = 0; // o contador de linhas da série (ex: da madrugada) volta a zero
            }
        }
    }

    public static void analisarSeriesMensal(){
        int i = 0, cont = 0, contMedia = 0; // i -> serve para ver se mudou de dia ou não || cont -> serve para fazer a média || contMedia -> serve para fazer o vetor media[]
        serie[i] = dados[0][CONSUMO];
        for (int linha = 1; linha < linhas; linha++) {
            if (dados[linha][MES] == dados[linha - 1][MES]) { // procura se muda de mês
                serie[i] += dados[linha][CONSUMO];
                cont++;
            } else if (dados[linha][MES] != dados[linha - 1][MES]) {
                media[contMedia] = serie[i] / cont;
                i++; // acrescentamos mais uma séria (mais um mês)
                serie[i] = dados[linha][CONSUMO];
                contMedia++; // é o número de médias
                cont = 0; // o contador de linhas da série (do Mês) volta a zero
            }
        }
    }
}