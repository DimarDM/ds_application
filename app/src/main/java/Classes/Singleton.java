package Classes;

import android.os.Build;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Singleton {

private Publisher publisher;

private Singleton() {
        publisher = new Publisher("127.0.0.1",2011,"127.0.0.1",5005,3);

}

public Publisher getPublisher() {
        return publisher;
}

public static Singleton singletonInstance;

public static Singleton getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new Singleton();
        }
        return singletonInstance;
    }

}
