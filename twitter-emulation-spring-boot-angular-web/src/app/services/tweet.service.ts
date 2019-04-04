import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Tweet } from "../models/tweet.model";
import { catchError } from "rxjs/operators";
import { MessageService } from "../modules/message/message.service";

@Injectable({
  providedIn: 'root'
})
export class TweetService {
  private baseUrl = 'api/tweet/';

  constructor(private http: HttpClient, private messageService: MessageService) {
  }

  getTimeline(): Observable<Tweet[]> {
    return this.http.get<Tweet[]>(this.baseUrl + 'timeline').pipe(
      catchError((response: Response) => {
        this.messageService.reportMessage(response);
        throw response;
      })
    );
  }

  tweet(text: string): Observable<string> {
    return this.http.post<string>(this.baseUrl + 'tweets', text)
      .pipe(
        catchError((response: Response) => {
          this.messageService.reportMessage(response);
          throw response;
        })
      );
  }
}
