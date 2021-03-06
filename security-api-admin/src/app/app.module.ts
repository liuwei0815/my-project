import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
//发送http请求
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppComponent } from './app.component';
import { RefreshInterceptor } from './app.interceptor';

//使双向绑定生效
import { FormsModule } from '@angular/forms';
// angurla 操作cookie 需要这个插件
import { CookieService } from 'ngx-cookie-service';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,//引进
    HttpClientModule//引进
  ],
  providers: [
    CookieService,
    { provide: HTTP_INTERCEPTORS, useClass: RefreshInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
