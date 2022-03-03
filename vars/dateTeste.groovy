class Example { 
   static void main(String[] args) {       
      

    def data = new Date().format("yyyyMMdd:hhmm")

    
    def objeto = [ ]
    objeto.add([1,2])
    objeto.add([3,4,5])
    def objetos = [a: 1, b: 2, c: 3, d: 4, e: 5]

   

    println objeto[1][1]
    objeto.each { println it}

    objeto.eachWithIndex{ it,i -> println "$i : $it"}
    System.out.println(data.toString());

   } 
} 