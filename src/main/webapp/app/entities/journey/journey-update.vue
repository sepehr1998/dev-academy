<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="solitaApp.journey.home.createOrEditLabel" data-cy="JourneyCreateUpdateHeading">Create or edit a Journey</h2>
        <div>
          <div class="form-group" v-if="journey.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="journey.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="journey-distance">Distance</label>
            <input
              type="number"
              class="form-control"
              name="distance"
              id="journey-distance"
              data-cy="distance"
              :class="{ valid: !$v.journey.distance.$invalid, invalid: $v.journey.distance.$invalid }"
              v-model.number="$v.journey.distance.$model"
              required
            />
            <div v-if="$v.journey.distance.$anyDirty && $v.journey.distance.$invalid">
              <small class="form-text text-danger" v-if="!$v.journey.distance.required"> This field is required. </small>
              <small class="form-text text-danger" v-if="!$v.journey.distance.numeric"> This field should be a number. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="journey-duration">Duration</label>
            <input
              type="text"
              class="form-control"
              name="duration"
              id="journey-duration"
              data-cy="duration"
              :class="{ valid: !$v.journey.duration.$invalid, invalid: $v.journey.duration.$invalid }"
              v-model="$v.journey.duration.$model"
              required
            />
            <div v-if="$v.journey.duration.$anyDirty && $v.journey.duration.$invalid">
              <small class="form-text text-danger" v-if="!$v.journey.duration.required"> This field is required. </small>
              <small class="form-text text-danger" v-if="!$v.journey.duration.numeric"> This field should be a number. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="journey-departureStation">Departure Station</label>
            <select
              class="form-control"
              id="journey-departureStation"
              data-cy="departureStation"
              name="departureStation"
              v-model="journey.departureStation"
              required
            >
              <option v-if="!journey.departureStation" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  journey.departureStation && stationOption.id === journey.departureStation.id ? journey.departureStation : stationOption
                "
                v-for="stationOption in stations"
                :key="stationOption.id"
              >
                {{ stationOption.name }}
              </option>
            </select>
          </div>
          <div v-if="$v.journey.departureStation.$anyDirty && $v.journey.departureStation.$invalid">
            <small class="form-text text-danger" v-if="!$v.journey.departureStation.required"> This field is required. </small>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="journey-returnStation">Return Station</label>
            <select
              class="form-control"
              id="journey-returnStation"
              data-cy="returnStation"
              name="returnStation"
              v-model="journey.returnStation"
              required
            >
              <option v-if="!journey.returnStation" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  journey.returnStation && stationOption.id === journey.returnStation.id ? journey.returnStation : stationOption
                "
                v-for="stationOption in stations"
                :key="stationOption.id"
              >
                {{ stationOption.name }}
              </option>
            </select>
          </div>
          <div v-if="$v.journey.returnStation.$anyDirty && $v.journey.returnStation.$invalid">
            <small class="form-text text-danger" v-if="!$v.journey.returnStation.required"> This field is required. </small>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.journey.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./journey-update.component.ts"></script>
