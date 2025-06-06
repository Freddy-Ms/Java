import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IInstructorApp } from '../instructor-app.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../instructor-app.test-samples';

import { InstructorAppService, RestInstructorApp } from './instructor-app.service';

const requireRestSample: RestInstructorApp = {
  ...sampleWithRequiredData,
  hireDate: sampleWithRequiredData.hireDate?.toJSON(),
};

describe('InstructorApp Service', () => {
  let service: InstructorAppService;
  let httpMock: HttpTestingController;
  let expectedResult: IInstructorApp | IInstructorApp[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InstructorAppService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a InstructorApp', () => {
      const instructor = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(instructor).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InstructorApp', () => {
      const instructor = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(instructor).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InstructorApp', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InstructorApp', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InstructorApp', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInstructorAppToCollectionIfMissing', () => {
      it('should add a InstructorApp to an empty array', () => {
        const instructor: IInstructorApp = sampleWithRequiredData;
        expectedResult = service.addInstructorAppToCollectionIfMissing([], instructor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(instructor);
      });

      it('should not add a InstructorApp to an array that contains it', () => {
        const instructor: IInstructorApp = sampleWithRequiredData;
        const instructorCollection: IInstructorApp[] = [
          {
            ...instructor,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInstructorAppToCollectionIfMissing(instructorCollection, instructor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InstructorApp to an array that doesn't contain it", () => {
        const instructor: IInstructorApp = sampleWithRequiredData;
        const instructorCollection: IInstructorApp[] = [sampleWithPartialData];
        expectedResult = service.addInstructorAppToCollectionIfMissing(instructorCollection, instructor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(instructor);
      });

      it('should add only unique InstructorApp to an array', () => {
        const instructorArray: IInstructorApp[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const instructorCollection: IInstructorApp[] = [sampleWithRequiredData];
        expectedResult = service.addInstructorAppToCollectionIfMissing(instructorCollection, ...instructorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const instructor: IInstructorApp = sampleWithRequiredData;
        const instructor2: IInstructorApp = sampleWithPartialData;
        expectedResult = service.addInstructorAppToCollectionIfMissing([], instructor, instructor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(instructor);
        expect(expectedResult).toContain(instructor2);
      });

      it('should accept null and undefined values', () => {
        const instructor: IInstructorApp = sampleWithRequiredData;
        expectedResult = service.addInstructorAppToCollectionIfMissing([], null, instructor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(instructor);
      });

      it('should return initial array if no InstructorApp is added', () => {
        const instructorCollection: IInstructorApp[] = [sampleWithRequiredData];
        expectedResult = service.addInstructorAppToCollectionIfMissing(instructorCollection, undefined, null);
        expect(expectedResult).toEqual(instructorCollection);
      });
    });

    describe('compareInstructorApp', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInstructorApp(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 14207 };
        const entity2 = null;

        const compareResult1 = service.compareInstructorApp(entity1, entity2);
        const compareResult2 = service.compareInstructorApp(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 14207 };
        const entity2 = { id: 32448 };

        const compareResult1 = service.compareInstructorApp(entity1, entity2);
        const compareResult2 = service.compareInstructorApp(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 14207 };
        const entity2 = { id: 14207 };

        const compareResult1 = service.compareInstructorApp(entity1, entity2);
        const compareResult2 = service.compareInstructorApp(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
