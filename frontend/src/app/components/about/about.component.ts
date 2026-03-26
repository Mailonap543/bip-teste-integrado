import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})
export class AboutComponent {
  title = 'Sobre o Sistema';
  version = '1.0.0';

  techStack = [
    { name: 'Angular 20', desc: 'Frontend Framework' },
    { name: 'Spring Boot 3.2.5', desc: 'Backend Framework' },
    { name: 'PostgreSQL', desc: 'Database' },
    { name: 'Jakarta EJB', desc: 'Enterprise JavaBeans' },
    { name: 'Swagger/OpenAPI', desc: 'API Documentation' }
  ];
}
