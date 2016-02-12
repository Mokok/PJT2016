package entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-02-07T11:37:30.722+0100")
@StaticMetamodel(Video.class)
public class Video_ {
	public static volatile SingularAttribute<Video, Long> id;
	public static volatile SingularAttribute<Video, String> nameInput;
	public static volatile SingularAttribute<Video, String> nameOutput;
	public static volatile SingularAttribute<Video, String> extInput;
	public static volatile SingularAttribute<Video, String> extOutput;
	public static volatile SingularAttribute<Video, User> user;
}
