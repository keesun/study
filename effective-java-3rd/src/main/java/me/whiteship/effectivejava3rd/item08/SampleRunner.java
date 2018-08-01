package me.whiteship.effectivejava3rd.item08;

public class SampleRunner {

    public static void main(String[] args) {
        try(var sampleResource = new SampleResource()) {
            sampleResource.hello();
        }
    }

}
