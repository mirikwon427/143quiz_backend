package garlicbears.quiz.domain.common.entity;

import static garlicbears.quiz.domain.common.entity.Active.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import garlicbears.quiz.domain.game.common.entity.Reward;
import garlicbears.quiz.domain.game.common.entity.UserAnswer;
import garlicbears.quiz.global.exception.CustomException;
import garlicbears.quiz.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends BaseTimeEntity implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_seq")
	private long userId;

	@Column(name = "user_email", nullable = false, length = 200)
	private String userEmail;

	@Column(name = "user_password", nullable = false, length = 300)
	private String userPassword;

	@Column(name = "user_nickname", nullable = false, length = 100)
	private String userNickname;

	@Column(name = "user_birth_year", nullable = false)
	private int userBirthYear;

	@Column(name = "user_age", nullable = false)
	private int userAge;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_gender", nullable = false)
	private Gender userGender;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_location", nullable = false)
	private Location userLocation;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_active", nullable = false)
	private Active userActive = active;

	@OneToMany(mappedBy = "user")
	private List<Reward> reward = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<UserAnswer> userAnswers = new ArrayList<>();

	@ManyToMany
	@JoinTable(name = "user_role",
		joinColumns = @JoinColumn(name = "user_seq"),
		inverseJoinColumns = @JoinColumn(name = "role_seq")
	)
	private Set<Role> roles = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "user_image_seq")
	private Image image;

	public User() {
	}

	public User(String userEmail, long userId) {
		this.userEmail = userEmail;
		this.userId = userId;
	}

	public User(UserBuilder builder) {
		this.userEmail = builder.userEmail;
		this.userPassword = builder.userPassword;
		this.userNickname = builder.userNickname;
		this.userBirthYear = builder.userBirthYear;
		this.userAge = builder.userAge;
		this.userGender = builder.userGender;
		this.userLocation = builder.userLocation;
		this.roles = builder.roles;
		this.image = builder.image;
	}

	public Long getUserId() {
		return userId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public int getUserBirthYear() {
		return userBirthYear;
	}

	public int getUserAge() {
		return userAge;
	}

	public Gender getUserGender() {
		return userGender;
	}

	public Location getUserLocation() {
		return userLocation;
	}

	public Active getUserActive() {
		return userActive;
	}

	public List<Reward> getRewards() {
		return reward;
	}

	public List<UserAnswer> getUserAnswers() {
		return userAnswers;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public Image getImage() {
		return image;
	}

	public void setUserBirthYear(int userBirthYear) {
		this.userBirthYear = userBirthYear;
	}

	public void setUserAge(int userAge) {
		this.userAge = userAge;
	}

	public void setUserGender(Gender userGender) {
		this.userGender = userGender;
	}

	public void setUserLocation(Location userLocation) {
		this.userLocation = userLocation;
	}

	public void setUserActive(Active userActive) {
		this.userActive = userActive;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream()
			.map(role -> new SimpleGrantedAuthority(role.getRoleName()))
			.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return getUserPassword();
	}

	@Override
	public String getUsername() {
		return getUserEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public static class UserBuilder {
		private final String userEmail;
		private final String userPassword;
		private final String userNickname;
		private int userBirthYear;
		private int userAge;
		private Gender userGender;
		private Location userLocation;
		private Set<Role> roles = new HashSet<>();
		private Image image;

		public UserBuilder(String userEmail, String userPassword, String userNickname) {
			this.userEmail = userEmail;
			this.userPassword = userPassword;
			this.userNickname = userNickname;
		}

		public UserBuilder userBirthYear(int userBirthYear) {
			this.userBirthYear = userBirthYear;
			return this;
		}

		public UserBuilder userAge(int userAge) {
			this.userAge = userAge;
			return this;
		}

		public UserBuilder userGender(Gender userGender) {
			this.userGender = userGender;
			return this;
		}

		public UserBuilder userLocation(Location userLocation) {
			this.userLocation = userLocation;
			return this;
		}

		public UserBuilder userRole(Role role) {
			this.roles.add(role);
			return this;
		}

		public UserBuilder userImage(Image image) {
			this.image = image;
			return this;
		}

		public User build() {
			return new User(this);
		}
	}

	public enum Gender {
		male,
		female,
		other;
	}

	public enum Location {
		Seoul,
		Gangwon,
		Gyeonggi,
		Incheon,
		Chungcheong,
		Jeolla,
		Gyeongsang,
		Jeju,
		Overseas;
	}
}
