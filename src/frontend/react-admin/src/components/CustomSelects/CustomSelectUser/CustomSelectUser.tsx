import React, { useState } from 'react';
import Indicator from '../../../pages/Authentication/components/Indicator';
import '../CustomSelectInput.css'
import { CustomSelectInputProps } from '../CustomSelectProps';
import { UserResponse } from '../../../generated';


const CustomSelectUser: React.FC<CustomSelectInputProps> = ({
  inputLabel,
  inputPlaceholder,
  required,
  wrapperStyle,
  labelStyle,
  inputStyle,
  maxHeightList,
  matchList,
  selectOptionEvent,
  typingInputEvent
}) => {

  const [inputValue, setInputValue] = useState<string>();
  const [isDropdownOpen, setIsDropdownOpen] = useState<boolean>(false);
  const [showIndicator, setShowIndicator] = useState<boolean>(false);


  const handleOptionSelect = (selectedValue: UserResponse) => {
    setInputValue(selectedValue.nom + ' ' + selectedValue.prenom + ' ' + selectedValue.email + ' ' + selectedValue.tel);
    setIsDropdownOpen(false);
    selectOptionEvent(selectedValue);
    setShowIndicator(false)
  };

  //const isEmpty = () => matchList.length === 0;

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = event.target;
    setInputValue(value);
    setShowIndicator(value !== '');
    setIsDropdownOpen(true);
    typingInputEvent(value);
  };
  const handleRemoveOption = () => {
    setInputValue('')
  }

  const maxItemsToShow = 3; // Nombre maximal d'éléments à afficher sans scroller
  const totalItems = matchList.length;

  // Calcul du pourcentage en fonction du nombre d'éléments
  const topPercentage = Math.min((totalItems - maxItemsToShow) * 100, 440) * (-1);


  return (
    <div className={`${wrapperStyle} relative`} id="input-wrapper">
      <label htmlFor="input-label" className={`${labelStyle}`}>
        {inputLabel} {required && <span className="text-meta-1">*</span>}
      </label>
      <div className='flex'>
        <input
          type="text"
          className={`${inputStyle} w-full`}
          value={inputValue}
          placeholder={inputPlaceholder}
          id="input-label"
          autoComplete="off"
          required
          onChange={handleInputChange}
        />
        {inputValue && <button onClick={handleRemoveOption} className='absolute self-center text-end right-5'>
          &times;
        </button>}
      </div>
      {showIndicator &&
        <div className="absolute right-4 top-1/2 transform -translate-y-1/2 mt-4">
          <Indicator widtf={5} height={5} border="blue" />
        </div>}
      <div
        className="custom-input-dropdown w-full"
      >
        <ul>
          {isDropdownOpen && inputValue !== '' && (
            <div
              className={`custom-input-dropdown w-full block`}
            >
              <ul
                style={{ maxHeight: `${maxHeightList}px`, overflowY: 'auto' }}
                className={`absolute top-${topPercentage}% w-full border border-gray-300 rounded-md bg-white shadow-md custom-select-ul`}
              >
                {matchList.length > 0 ? matchList.map((option: UserResponse, index) => (
                  <li
                    className="dark:border-form-strokedark bg-red dark:bg-form-input px-4 py-1 hover:bg-red-600 cursor-pointer w-full custom-select-li"
                    key={index}
                    onClick={() => handleOptionSelect(option)}
                  >
                    {option.nom + ' ' + option.prenom + ' ' + option.email}
                  </li>
                )) :
                  <li
                    className="dark:border-form-strokedark bg-red dark:bg-form-input px-4 hover:bg-red-600 justify-center text-center py-2 w-full"
                  >
                    {'--- Aucun user n\'a ete trouve ---'}
                  </li>
                }
              </ul>
            </div>
          )}
        </ul>
      </div>
    </div>
  );
};

export default CustomSelectUser;
