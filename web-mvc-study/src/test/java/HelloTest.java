import org.junit.Test;

public class HelloTest {


    interface AHello{
        void hello() ;
    }

    interface BHello{
        void hello() ;
    }
    class AHelloImpl implements AHello{
        @Override
        public void hello(){
            System.out.println("hello A");
        }
    }

    class BHelloImpl implements BHello{
        @Override
        public void hello(){
            System.out.println("hello B");
        }
    }

    class ABHelloAdapter implements AHello, BHello{
        private AHello aHello ;
        private BHello bHello ;
        public ABHelloAdapter(AHello aHello, BHello bHello){
            this.aHello = aHello ;
            this.bHello = bHello ;
        }
        @Override
        public void hello() {
            if (this instanceof AHello){
                aHello.hello();
            }

            if(this instanceof BHello) {
                bHello.hello();
            }
        }
    }

    @Test
    public void test1(){
        AHello aHello ;
        BHello bHello ;
        ABHelloAdapter abHelloAdapter = new ABHelloAdapter(new AHelloImpl(), new BHelloImpl()) ;
        //abHelloAdapter.hello();
        bHello = abHelloAdapter ;
        bHello.hello();
    }
}
