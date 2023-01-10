/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import JourneyUpdateComponent from '@/entities/journey/journey-update.vue';
import JourneyClass from '@/entities/journey/journey-update.component';
import JourneyService from '@/entities/journey/journey.service';

import StationService from '@/entities/station/station.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Journey Management Update Component', () => {
    let wrapper: Wrapper<JourneyClass>;
    let comp: JourneyClass;
    let journeyServiceStub: SinonStubbedInstance<JourneyService>;

    beforeEach(() => {
      journeyServiceStub = sinon.createStubInstance<JourneyService>(JourneyService);

      wrapper = shallowMount<JourneyClass>(JourneyUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          journeyService: () => journeyServiceStub,
          alertService: () => new AlertService(),

          stationService: () =>
            sinon.createStubInstance<StationService>(StationService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.journey = entity;
        journeyServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(journeyServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.journey = entity;
        journeyServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(journeyServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundJourney = { id: 123 };
        journeyServiceStub.find.resolves(foundJourney);
        journeyServiceStub.retrieve.resolves([foundJourney]);

        // WHEN
        comp.beforeRouteEnter({ params: { journeyId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.journey).toBe(foundJourney);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
