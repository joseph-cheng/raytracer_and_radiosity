package src.scene;

import src.model.Model;
import src.math.Vec3;
import src.math.Mat4;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SceneLoader {
    private Scene scene;

    public SceneLoader(String filename) {
        scene = new Scene();

        if (!xml_file.endsWith(".xml")) {
            throw new IllegalArgumentException("File is not an .xml file");
        }

        Element document;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(xml_file)).getDocumentElement();
        }
        catch (ParserConfigurationException e) {
            assert false;
        }
        catch (IOException e) {
            throw new RuntimeException("Error reading file:\n" + e.getMessage());
        }
        catch (SAXException e){
            throw new RuntimeException("Error loading XMl");
        }

        if (document.getNodeName() != "scene") {
            throw new RuntimeException("Scene file does not contain a scene element");
        }

        for (Element element : document.getChildNodes()) {
            switch (element.getNodeName()) {
                case "object":
                    scene.add_object(get_object(element));
                    break;
                case "point-light":
                    PointLight light = new PointLight(get_vec(element), get_colour(element), get_double(element, "intensity", 100));
                    scene.add_light(light);
                    break;

                case "ambient-light":
                    scene.set_ambient_light(get_colour(element), get_double(element, "intensity", 1.0));
                    break;

                default:
                    throw new RuntimeException("Unknown object tag: " + element.getNodeName());
l
            }
        }
    }

    public Scene get_scene() {
        return scene;
    }

    private SceneObject get_object(Element object) {
        SceneObject obj = new SceneObject();

        for (Element element : object.getChildNodes()) {
            switch (element.getNodeName()) {
                case "model":
                    obj.set_model(new Model(element.getAttribute("file")));
                    break;

                case "material":
                    obj.set_colour(get_colour(element));
                    obj.set_diffuse(get_double(element, "diffuse", SceneObject.DEFAULT_KD));
                    obj.set_specular(get_double(element, "specular", SceneObject.DEFAULT_KS));
                    obj.set_alpha(get_double(element, "alpha", SceneObject.DEFAULT_ALPHA));
                    obj.set_reflectivity(get_double(element, "reflectivity", SceneObject.DEFAULT_REFLECTIVITY));
                    break;

                case "scale":
                    obj.scale(get_vec(element));
                    break;

                case "rotation":
                    obj.rotate(get_double(element, "angle", 0.0), get_vec(element));
                    break;

                case "translation":
                    obj.translate(get_vec(element));
                    break;
                
                default:
                    throw new RuntimeException("Unknown object tag: " + element.getNodeName());
            }
        }
        obj.transform_model();
        return obj;
    }

    private Colour get_colour(Element tag) {
        String hex_colour = tag.getAttribute("colour");
        double r = Integer.parseInt(hexString.substring(1, 3), 16) / 255.0;
        double g = Integer.parseInt(hexString.substring(3, 5), 16) / 255.0;
        double b = Integer.parseInt(hexString.substring(5, 7), 16) / 255.0;

        return new Colour(r,g,b);
    }

    private Vec3 get_vec(Element tag) {
        double x = get_double(tag, "x", 0.0);
        double y = get_double(tag, "y", 0.0);
        double z = get_double(tag, "z", 0.0);
        return new Vec3(x,y,z);
    }

    private double get_double(Element tag, String attribute, double fallback) {
        try {
            return Double.parseDouble(tag.getAttribute(attribute));
        }
        catch (NumberFormatException e) {
            return fallback;
        }
    }
}
