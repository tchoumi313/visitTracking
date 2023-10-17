import { BureauResponse } from "../../generated";

export const SET_BUREAUX = 'SET_BUREAUX';
export const SET_BUREAU = 'SET_BUREAU';
export const CREATE_BUREAU = 'CREATE_BUREAU';
export const UPDATE_BUREAU = 'UPDATE_BUREAU';
export const DELETE_BUREAU = 'DELETE_BUREAU';

export const setBureaux = (bureaux: BureauResponse[]) => ({
  type: SET_BUREAUX,
  payload: bureaux,
});

export const setBureau = (bureau: BureauResponse) => ({
  type: SET_BUREAU,
  payload: bureau,
});

export const createBureau = (bureau: BureauResponse) => ({
  type: CREATE_BUREAU,
  payload: bureau,
});

export const updateBureau = (bureau: BureauResponse) => ({
  type: UPDATE_BUREAU,
  payload: bureau,
});

export const deleteBureau = (bureau: BureauResponse) => ({
  type: DELETE_BUREAU,
  payload: bureau,
});
