package spring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class MainForSpring {
	
	//각각 xml에 있는 데이터들을 ctxs에 배열형식으로 담음
	//appCtx에 2,3을 임포트해서 사용하면 배열 만들필요 x
	static String[] ctxs = {"classpath:appCtx.xml",
							"classpath:appCtx2.xml",
							"classpath:appCtx3.xml"};
	
	// 배열에 들어있는 데이터를 불러옴		배열을 사용하려면 경로대신 ctxs를 입력
	static ApplicationContext ctx = new GenericXmlApplicationContext("classpath:appCtx.xml");

	public static void main(String[] args) throws IOException {
		
		
		/*System.out.println("객체생성 - 라이프 사이클 확인");
		System.out.println("--------------------");
		MemberDao mdao1 = ctx.getBean("memberDao",MemberDao.class);
		MemberDao mdao2 = ctx.getBean("memberDao",MemberDao.class);
		
		System.out.println("비교 mdao1 == mdao2 ==> " + (mdao1 == mdao2));
		
		System.out.println("====================");*/
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.println("명렁어를 입력하세요:");
			String command = reader.readLine();
			if (command.equalsIgnoreCase("exit")) {
				System.out.println("종료합니다.");
				break;
			}
			
			// new qetuo122@naver.com 김태원 1111
			if (command.startsWith("new ")) {
				processNewCommand(command.split(" "));
				continue;
			} else if (command.startsWith("change ")) {
				processChangeCommand(command.split(" "));
				continue;
			}
			printHelp();
		}
	}

	private static void processNewCommand(String[] arg) {
		if (arg.length != 5) {
			printHelp();
			return;
		}
		
		
		MemberRegisterService regSvc = ctx.getBean("memberRegSvc",MemberRegisterService.class);
		
		
		RegisterRequest req = new RegisterRequest();
		req.setEmail(arg[1]);
		req.setName(arg[2]);
		req.setPassword(arg[3]);
		req.setConfirmPassword(arg[4]);
		if (!req.isPasswordEqualToConfirmPassword()) {
			System.out.println("암호와 확인이 일치하지 않습니다.\n");
			return;
		}
		try {
			regSvc.regist(req);
			System.out.println("등록했습니다.\n");
		} catch (AlreadyExistingMemberException e) {
			System.out.println("이미 존재하는 이메일입니다.\n");
		}
	}
	
	// change qetuo122@naver.com 111 333
	private static void processChangeCommand(String[] arg) {
		if (arg.length != 4) {
			printHelp();
			return;
		}
		
		ChangePasswordService changePwdSvc = ctx.getBean("changePwdSvc",ChangePasswordService.class);
		
		try {
			changePwdSvc.changePassword(arg[1], arg[2], arg[3]);
			System.out.println("암호를 변경했습니다.\n");
		} catch (MemberNotFoundException e) {
			System.out.println("존재하지 않는 이메일입니다.\n");
		} catch (IdPasswordNotMatchingException e) {
			System.out.println("이메일과 암호가 일치하지 않습니다.\n");
		}
	}

	private static void printHelp() {
		System.out.println();
		System.out.println("잘못된 명령입니다. 아래 명령어 사용법을 확인하세요.");
		System.out.println("명령어 사용법:");
		System.out.println("new 이메일 이름 암호 암호확인");
		System.out.println("change 이메일 현재비번 변경비번");
		System.out.println();
	}

}
