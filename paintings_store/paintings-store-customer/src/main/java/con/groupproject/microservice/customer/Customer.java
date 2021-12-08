package con.groupproject.microservice.customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import javax.validation.constraints.Email;

@Entity
public class Customer {

    @Id
    @Getter
    @Setter
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Getter
    @Setter
    private String name;

    @Column(nullable = false)
    @Getter
    @Setter
    private String firstname;

    @Column(nullable = false)
    @Email
    @Getter
    @Setter
    private String email;

    @Column(nullable = false)
    @Getter
    @Setter
    private String street;

    @Column(nullable = false)
    @Getter
    @Setter
    private String city;

    public Customer() {
        super();
        id = 0l;
    }

    public Customer(String firstname, String name, String email, String street,
                    String city) {
        super();
        this.name = name;
        this.firstname = firstname;
        this.email = email;
        this.street = street;
        this.city = city;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);

    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

}

