public class TestDI {

    @dagger.Component(modules = LaptopModule.class)
    interface LaptopComponent {
        @MacBookAir
        Laptop macBookAir();

        @MacBookPro
        Laptop macBookPro();

        @MacBookProXL
        Laptop macBookProXL();


    }


    public static void main(String[] args) {
        LaptopComponent daggerGeneratedComponent = DaggerTestDI_LaptopComponent.builder().build();
        @MacBookAir Laptop macBookAir = daggerGeneratedComponent.macBookAir();
        @MacBookPro Laptop macBookPro = daggerGeneratedComponent.macBookPro();
        @MacBookProXL Laptop macBookProXL = daggerGeneratedComponent.macBookProXL();
    }

}

@dagger.Module
class LaptopModule {

    private Laptop laptop;

    @dagger.Provides
    @MacBookAir
    Laptop provideMacBookAir() {
        return new Laptop(new SeagateHDD(), new HitachiBattery(new Capacity(5400)));
    }

    @dagger.Provides
    @MacBookPro
    Laptop provideMacBookPro() {
        return new Laptop(new SeagateSSD(), new LGBattery(new CapacityV2(3400)));
    }

    @dagger.Provides
    @MacBookProXL
    Laptop provideMacBookProXL() {
        return new Laptop(new SeagateHDD(), new LGBattery(new CapacityV2(2400)));
    }
}


class Laptop {
    Storage storage;
    Battery battery;

    public Laptop(Storage storage, Battery battery) {
        this.storage = storage;
        this.battery = battery;
    }
}


class SeagateHDD implements Storage {
    public SeagateHDD() {
        System.out.println("Seagate's HDD installed");
    }
}

class HitachiBattery implements Battery {
    Capacity cap;

    public HitachiBattery(Capacity cap) {
        this.cap = cap;
        System.out.println("Hitachi's Battery Installed with capacity: " + cap.number);
    }

}

class SeagateSSD implements Storage {

    public SeagateSSD() {
        System.out.println("Seagate's SSD installed");
    }
}


class LGBattery implements Battery {
    CapacityV2 cap;

    public LGBattery(CapacityV2 cap) {
        this.cap = cap;
        System.out.println("LG's Battery Installed with capacity: " + cap.number);
    }
}

interface Battery {

}

interface Storage {

}

class Capacity {
    int number;

    public Capacity(int number) {
        this.number = number;
    }
}

class CapacityV2 {
    int number;

    public CapacityV2(int number) {
        this.number = number;
    }
}