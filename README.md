## Status of project

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Kakaranish_e-biznes&metric=alert_status)](https://sonarcloud.io/dashboard?id=Kakaranish_e-biznes)

## How to run app?

Fill social providers info in `scala-project/conf/silhouette.conf`. "BLAH BLAH BLAH" means placeholder for your data. The complete `silhouette.conf` should look like this: 
<pre>
silhouette {
  socialStateHandler.signer.key = "[aRe7QuPXuNNdPasdas7qPLhk3vg12312smy]"

  authenticator.headerName="X-Auth-Token"
  authenticator.issuerClaim="play-silhouette"
  authenticator.encryptSubject=true
  authenticator.authenticatorExpiry=12 hours
  authenticator.sharedSecret="EWpJtyGhwuFe4KZB2rW35cHNx4tZDpLf"
  authenticator.crypter.key = "[v2hAkZkwsVYddyyVqBQdNrCfuBdPrT2S]"

  csrfStateItemHandler.cookieName="OAuth2State"
  csrfStateItemHandler.cookiePath="/"
  csrfStateItemHandler.secureCookie=false
  csrfStateItemHandler.httpOnlyCookie=true
  csrfStateItemHandler.sameSite="Lax"
  csrfStateItemHandler.expirationTime=5 minutes
  csrfStateItemHandler.signer.key = "5bD6bkhjasd78asa32sJgkwzsd28htEU2WMc"

  google.authorizationURL="https://accounts.google.com/o/oauth2/auth"
  google.accessTokenURL="https://accounts.google.com/o/oauth2/token"
  google.redirectURL="http://localhost:9000/auth/provider/google"
  google.clientID=<b>"126123-q691casduyhs7ol27da8khhumumlbq7g.apps.googleusercontent.com"</b>
  google.clientSecret=<b>"7fwExUDS87dkk9c4xhplkWEKS5s"</b>
  google.scope="profile email"

  facebook.authorizationURL="https://graph.facebook.com/v2.3/oauth/authorize"
  facebook.accessTokenURL="https://graph.facebook.com/v2.3/oauth/access_token"
  facebook.redirectURL="http://localhost:9000/auth/provider/facebook"
  facebook.clientID=<b>"28623127891047812312"</b>
  facebook.clientSecret=<b>"e7534712vbh2dd2badc07123274a27"</b>
  facebook.scope="email"

  # Data above is fake :)
}
</pre>

NOTE: By default database will be created under `./scala-project/data` path. You can easily change it in `docker-compose.yaml`.