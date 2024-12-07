import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SavedProjectsComponent } from './saved-projects.component';

describe('SavedProjectsComponent', () => {
  let component: SavedProjectsComponent;
  let fixture: ComponentFixture<SavedProjectsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SavedProjectsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SavedProjectsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
