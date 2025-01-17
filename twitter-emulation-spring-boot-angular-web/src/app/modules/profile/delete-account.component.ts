import { Component } from '@angular/core';
import { Router } from "@angular/router";
import { User } from "../../shared/models/user.model";
import { AuthenticationService } from "../../shared/services/authentication.service";
import { AccountService } from "../../shared/services/account.service";

@Component({
  selector: 'app-delete-account',
  templateUrl: './delete-account.component.html'
})
export class DeleteAccountComponent {
  private user: User = new User();

  constructor(private authenticationService: AuthenticationService, private accountService: AccountService, private router: Router) {
    authenticationService.getUser().subscribe(data => {
      this.user = data;
    });
  }

  delete() {
    this.accountService.deleteAccount(this.user.name)
      .subscribe(data => {
        this.router.navigate(['/login'], {
          queryParams: {
            "logout": true
          }
        });
      });
  }
}
