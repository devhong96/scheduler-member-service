package com.scheduler.memberservice.infra.email.application;

public final class FindUsernameHtml {

    private FindUsernameHtml() {}

    /** 카드형 HTML 본문 */
    public static String html(String requestEmail, String username) {
        return """
                <!doctype html>
                <html lang="ko">
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width"/>
                  <title>아이디 찾기 결과</title>
                </head>
                <body style="margin:0;padding:0;background-color:#f5f6f8;">

                  <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f5f6f8;">
                    <tr>
                      <td align="center" style="padding:24px;">
                        <table role="presentation" width="600" cellpadding="0" cellspacing="0"
                               style="width:600px;max-width:600px;background:#ffffff;border-radius:12px;
                                      border:1px solid #e7e8ea;">
                          <tr>
                            <td align="center" style="padding:36px 28px 0 28px;font-family:Arial, Helvetica, sans-serif;">
                              <div style="font-size:22px;font-weight:700;color:#202124;">아이디 찾기</div>
                            </td>
                          </tr>

                          <tr>
                            <td style="padding:16px 28px 0 28px;font-family:Arial, Helvetica, sans-serif;
                                       font-size:14px;line-height:22px;color:#444;">
                              %s 로 조회한 결과, 아이디는 아래와 같습니다.
                            </td>
                          </tr>

                          <tr>
                            <td align="center" style="padding:16px 28px 16px 28px;">
                              <div style="padding:12px 16px;border:1px solid #e7e8ea;border-radius:8px;
                                          font-family:Arial, Helvetica, sans-serif;font-size:18px;
                                          font-weight:700;color:#202124;display:inline-block;">
                                %s
                              </div>
                            </td>
                          </tr>

                          <tr>
                            <td style="padding:0 28px 24px 28px;font-family:Arial, Helvetica, sans-serif;
                                       font-size:12px;color:#6b7280;">
                              본인이 요청하지 않았다면 이 메일을 무시해 주세요.
                            </td>
                          </tr>

                          <tr>
                            <td style="padding:16px 28px 28px 28px;border-top:1px solid #eee;
                                       font-family:Arial, Helvetica, sans-serif;font-size:11px;color:#9aa0a6;">
                              © dev.hong96 scheduler
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
                """.formatted(htmlEscape(requestEmail), htmlEscape(username));
    }

    /** 텍스트 대안(권장) */
    public static String plain(String requestEmail, String username) {
        return "[아이디 찾기]\n\n" +
                "조회 이메일: " + requestEmail + "\n" +
                "사용자 이름: " + username + "\n\n" +
                "본인이 요청하지 않았다면 이 메일을 무시해 주세요.";
    }

    private static String htmlEscape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
