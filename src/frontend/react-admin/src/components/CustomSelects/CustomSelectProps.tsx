export interface CustomSelectInputProps {
    inputLabel: string;
    inputPlaceholder: string;
    required: boolean,
  
    wrapperStyle?: string;
    labelStyle?: string;
    inputStyle?: string;
    
    maxHeightList?: number;
    matchList: Object[];
    selectOptionEvent: (option: Object) => void;
    typingInputEvent: (inputValue: string) => void;
}

export interface CustomMultiSelectInputProps {
  inputLabel: string;
  inputPlaceholder: string;
  required: boolean;

  wrapperStyle?: string;
  labelStyle?: string;
  inputStyle?: string;

  maxHeightList?: number;
  matchList: Object[];
  selectOptionEvent: (options: Object[]) => void;
  typingInputEvent: (inputValue: string) => void;
}