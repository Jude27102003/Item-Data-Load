import java.util.Scanner;

public static boolean isPallindrome (String str) {
    String rev = new StringBuilder(str).reverse().toString();
    return str.equals(rev);
}

public static void main(String[] args) {
    Scanner sc=new Scanner(System.in);
    System.out.print("Enter the word to be checked: ");
    String input=sc.nextLine();

    if(isPallindrome(input)){
        System.out.println("The given word "+input+" is a pallindrome");

    }
    else {
        System.out.println("The given word "+input+" is not a pallindrome");
    }
    sc.close();
}
