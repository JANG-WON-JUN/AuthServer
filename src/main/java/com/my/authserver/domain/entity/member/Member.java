package com.my.authserver.domain.entity.member;

import static java.lang.Integer.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.my.authserver.domain.entity.BaseEntity;
import com.my.authserver.domain.entity.member.auth.MemberRoles;
import com.my.authserver.domain.entity.member.embedded.BirthDay;
import com.my.authserver.member.enums.Level;
import com.my.authserver.member.enums.Sex;
import com.my.authserver.member.enums.State;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEMBER_TB", indexes = {
	@Index(name = "idx_email", columnList = "email", unique = true),
	@Index(name = "idx_nickname", columnList = "nickname", unique = true)}
)
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "varchar(100)", nullable = false)
	private String email;

	@Column(columnDefinition = "varchar(100)")
	private String name;

	@Column(columnDefinition = "varchar(100)")
	private String nickname;

	private int levelPoint;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10)")
	private Level level;

	@Embedded
	private BirthDay birthDay;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(6)")
	private Sex sex;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(15)")
	private State state;

	@OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
	private Password password;

	@OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
	private RefreshToken refreshToken;

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<MemberLevelLog> memberLevelLogs = new ArrayList<>();

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<EmailAuth> emailAuths = new ArrayList<>();

	// 만약 mappedBy할 대상이 복합키를 가지고 있다면, (복합키 필드명.복합키안의 필드명) 이렇게 설정해주면 된다.
	@OneToMany(mappedBy = "id.member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<MemberRoles> memberRoles = new HashSet<>();

	@Builder
	private Member(String email, String name, String nickname,
		BirthDay birthDay, Sex sex, State state) {
		this.email = email;
		this.name = name;
		this.nickname = nickname;
		this.birthDay = birthDay;
		this.sex = sex;
		this.state = state;
	}

	public void setPassword(Password password) {
		this.password = password;
		password.setMember(this);
	}

	public void changeState(State state) {
		this.state = state;
	}

	public void changeRefreshToken(RefreshToken refreshToken) {
		this.refreshToken = refreshToken;
		refreshToken.setMember(this);
	}

	public void addRole(MemberRoles memberRole) {
		if (memberRoles.size() > 0) {
			memberRoles.remove(memberRole);
		}
		memberRoles.add(memberRole);
		memberRole.getId().setMember(this);
	}

	public int addLevelPoint() {
		return levelPoint + 1 < MAX_VALUE ? ++levelPoint : MAX_VALUE;
	}

	/**
	 * @return 레벨업 했을 때 true / 레벨업 안 했을 때 false
	 */
	public boolean levelUp() {
		Level level = Level.findLevel(levelPoint);
		if (this.level != level) {
			this.level = level;
			return true;
		}
		return false;
	}

	public void addEmailAuth(EmailAuth emailAuth) {
		if (emailAuths.size() > 0) {
			emailAuths.remove(emailAuth);
		}
		emailAuth.setMember(this);
		emailAuths.add(emailAuth);
	}
}