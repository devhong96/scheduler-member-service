package com.scheduler.memberservice.infra.email.application;

public final class VerificationHtml {

    private VerificationHtml() {}

    /** 카드형 HTML 본문 (로고 제거) */
    public static String html(String code, String ttlText) {
        return """
                <!doctype html>
                       <html lang="ko">
                       <head>
                           <meta charset="UTF-8">
                           <meta name="viewport" content="width=device-width"/>
                           <title>복구 인증번호 확인</title>
                       </head>
                       <body style="margin:0;padding:0;background-color:#f5f6f8;">
                       <!-- 프리헤더(미리보기) -->
                       <span style="display:none;visibility:hidden;opacity:0;color:transparent;height:0;width:0;overflow:hidden;">
                                           인증 코드를 입력해 설정을 완료하세요.
                                         </span>
                
                       <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f5f6f8;">
                           <tr>
                               <td align="center" style="padding:24px;">
                                   <!-- 카드 -->
                                   <table role="presentation" width="600" cellpadding="0" cellspacing="0"
                                          style="width:600px;max-width:600px;background:#ffffff;border-radius:12px;
                                                             border:1px solid #e7e8ea;">
                                       <tr>
                                           <td align="center" style="padding:28px 28px 0 28px;
                                                                             font-family:Arial, Helvetica, sans-serif;">
                                               <div style="font-size:22px;font-weight:700;color:#202124;">복구 이메일 확인</div>
                                           </td>
                                       </tr>
                
                                       <tr>
                                           <td style="padding:16px 28px 0 28px;font-family:Arial, Helvetica, sans-serif;
                                                              font-size:14px;line-height:22px;color:#444;">
                                               다음 코드를 입력하여 비빌번호 변경을 완료하세요.
                                           </td>
                                       </tr>
                
                                       <tr>
                                           <td align="center" style="padding:20px 28px 8px 28px;">
                                               <div style="font-family:Arial, Helvetica, sans-serif;font-size:40px;
                                                                 letter-spacing:6px;font-weight:700;color:#202124;">
                                                   %s
                                               </div>
                                           </td>
                                       </tr>
                
                                       <tr>
                                           <td align="center" style="padding:4px 28px 24px 28px;
                                                                             font-family:Arial, Helvetica, sans-serif;
                                                                             font-size:12px;color:#6b7280;">
                                               코드는 %s분 후 만료됩니다.
                                           </td>
                                       </tr>
                
                                       <tr>
                                           <td style="padding:0 28px 24px 28px;font-family:Arial, Helvetica, sans-serif;
                                                              font-size:12px;color:#6b7280;">
                                               본인이 아니라면 이 메일을 무시해 주세요.
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
                """.formatted(htmlEscape(code), htmlEscape(ttlText));
    }

    /** 플레인 텍스트 대안(권장) */
    public static String plain(
            String code, String ttlText
    ) {
        return "복구 이메일 확인\n\n" +
                "인증 코드: " + code + "\n" +
                "코드는 " + ttlText + " 후 만료됩니다.\n\n" +
                "본인이 아니라면 이 메일을 무시해 주세요.";
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
