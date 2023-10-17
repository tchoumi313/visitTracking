import { BureauResponse } from '../../generated';
import { SET_BUREAUX, CREATE_BUREAU, UPDATE_BUREAU, DELETE_BUREAU, SET_BUREAU } from '../Actions/BureauAction';

const initialState = {
  bureaux: [],
};

const BureauxReducers = (state = initialState, action : any) => {
  switch (action.type) {
    case SET_BUREAUX:
      return { ...state, bureaux: action.payload };

    case SET_BUREAU:
      return { ...state, bureau: action.payload };
  
    case CREATE_BUREAU:
      return { ...state, bureaux: [...state.bureaux, action.payload] };

    case UPDATE_BUREAU:
      return {
        ...state,
        bureaux: state.bureaux.map((bureau: BureauResponse) =>
          bureau.id === action.payload.id ? action.payload : bureau
        ),
      };

    case DELETE_BUREAU:
      return {
        ...state,
        bureaux: state.bureaux.filter((bureau: BureauResponse) => bureau.id !== action.payload),
      };

    default:
      return state;
  }
};

export default BureauxReducers;
