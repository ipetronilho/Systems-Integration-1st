//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.10.13 at 07:28:29 PM BST 
//

package blog.example_JMS;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="car" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="preco" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="anunciante" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="marca" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="modelo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="versao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="combustivel" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="quilometros" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="potencia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="cilindrada" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="cor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="lotacao" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "car"
})
@XmlRootElement(name = "cars")
public class Cars {

    protected List<Cars.Car> car;

    /**
     * Gets the value of the car property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the car property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCar().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Cars.Car }
     * 
     * 
     */
    public List<Cars.Car> getCar() {
        if (car == null) {
            car = new ArrayList<Cars.Car>();
        }
        return this.car;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="preco" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="anunciante" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="marca" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="modelo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="versao" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="combustivel" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="quilometros" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="potencia" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="cilindrada" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="cor" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="lotacao" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "preco",
        "anunciante",
        "marca",
        "modelo",
        "versao",
        "combustivel",
        "quilometros",
        "potencia",
        "cilindrada",
        "cor",
        "lotacao"
    })
    public static class Car {

        @XmlElement(required = true)
        protected String preco;
        @XmlElement(required = true)
        protected String anunciante;
        @XmlElement(required = true)
        protected String marca;
        @XmlElement(required = true)
        protected String modelo;
        @XmlElement(required = true)
        protected String versao;
        @XmlElement(required = true)
        protected String combustivel;
        @XmlElement(required = true)
        protected String quilometros;
        @XmlElement(required = true)
        protected String potencia;
        @XmlElement(required = true)
        protected String cilindrada;
        @XmlElement(required = true)
        protected String cor;
        @XmlElement(required = true)
        protected String lotacao;

        /**
         * Gets the value of the preco property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPreco() {
            return preco;
        }

        /**
         * Sets the value of the preco property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPreco(String value) {
            this.preco = value;
        }

        /**
         * Gets the value of the anunciante property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAnunciante() {
            return anunciante;
        }

        /**
         * Sets the value of the anunciante property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAnunciante(String value) {
            this.anunciante = value;
        }

        /**
         * Gets the value of the marca property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMarca() {
            return marca;
        }

        /**
         * Sets the value of the marca property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMarca(String value) {
            this.marca = value;
        }

        /**
         * Gets the value of the modelo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getModelo() {
            return modelo;
        }

        /**
         * Sets the value of the modelo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setModelo(String value) {
            this.modelo = value;
        }

        /**
         * Gets the value of the versao property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVersao() {
            return versao;
        }

        /**
         * Sets the value of the versao property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVersao(String value) {
            this.versao = value;
        }

        /**
         * Gets the value of the combustivel property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCombustivel() {
            return combustivel;
        }

        /**
         * Sets the value of the combustivel property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCombustivel(String value) {
            this.combustivel = value;
        }

        /**
         * Gets the value of the quilometros property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getQuilometros() {
            return quilometros;
        }

        /**
         * Sets the value of the quilometros property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setQuilometros(String value) {
            this.quilometros = value;
        }

        /**
         * Gets the value of the potencia property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPotencia() {
            return potencia;
        }

        /**
         * Sets the value of the potencia property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPotencia(String value) {
            this.potencia = value;
        }

        /**
         * Gets the value of the cilindrada property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCilindrada() {
            return cilindrada;
        }

        /**
         * Sets the value of the cilindrada property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCilindrada(String value) {
            this.cilindrada = value;
        }

        /**
         * Gets the value of the cor property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCor() {
            return cor;
        }

        /**
         * Sets the value of the cor property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCor(String value) {
            this.cor = value;
        }

        /**
         * Gets the value of the lotacao property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLotacao() {
            return lotacao;
        }

        /**
         * Sets the value of the lotacao property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLotacao(String value) {
            this.lotacao = value;
        }

    }

}
