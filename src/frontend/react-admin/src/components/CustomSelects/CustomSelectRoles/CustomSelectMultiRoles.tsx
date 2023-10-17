import React, { useState } from 'react';
import Indicator from '../../../pages/Authentication/components/Indicator';
import '../CustomSelectInput.css';
import { RiCheckLine } from 'react-icons/ri';
import { CustomMultiSelectInputProps } from '../CustomSelectProps';
import { RoleResponse } from '../../../generated';

const CustomSelectMultiRoles: React.FC<CustomMultiSelectInputProps> = ({
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
  const listeRoles: RoleResponse[] = matchList; // Recupere les Roles contenus dans l'objet matchList
  
  const [inputValue, setInputValue] = useState<string>('');
  const [selectedOptions, setSelectedOptions] = useState<RoleResponse[]>([]);
  const [isDropdownOpen, setIsDropdownOpen] = useState<boolean>(false);
  const [showIndicator, setShowIndicator] = useState<boolean>(false);
  const [ids, setIds] = useState<number[]>([]); // Tableau contennt les ids des roles selectionnes

  // fonction qui vérifie si un élément est déjà sélectionné :
  const isOptionSelected = (option: RoleResponse) => selectedOptions.includes(option);

  const [isSelected, setIsSelected] = useState<boolean>(); 

  const handleOptionSelect = (selectedOption: RoleResponse) => {
    setIsSelected(true);
    if (ids.includes(selectedOption.id!)) {
      setInputValue('');
      console.log('It\'s already selected')
      return; // Ne rien faire si l'option est déjà sélectionnée
    }
    setInputValue('');
    setSelectedOptions([...selectedOptions, selectedOption]);
    selectOptionEvent([...selectedOptions, selectedOption]);
    setIds([...ids, selectedOption.id!])
  };

  const handleOptionRemove = (removedOption: RoleResponse) => {
    const updatedOptions = selectedOptions.filter(option => option !== removedOption);
    const newIds = ids.filter(id => id !== removedOption.id)
    setIds(newIds);
    setSelectedOptions(updatedOptions);
    selectOptionEvent(updatedOptions);
    if (selectedOptions.length === 1) {
      setIsSelected(false);
    }
  };

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = event.target;
    
    setInputValue(value);
    setIsDropdownOpen(false);
    setShowIndicator(value !== '');
    setIsDropdownOpen(true);
    typingInputEvent(value);
  };

  const isEmpty = () => listeRoles.length === 0;

  const maxItemsToShow = 3; // Nombre maximal d'éléments à afficher sans scroller
  const totalItems = listeRoles.length;
  const topPercentage = Math.min((totalItems - maxItemsToShow) * 100, 440) * -1;

  return (
    <div className={`${wrapperStyle} relative`} id="input-wrapper">
      <label htmlFor="input-label" className={`${labelStyle}`}>
        {inputLabel} {required && <span className="text-meta-1">*</span>}
      </label>
      <div className="selected-options-wrapper mb-2">
  
    <div className='selected-options-wrapper-custom flex'>
      {selectedOptions.map((option, index) => (
          <div key={index} className="flex items-center space-x-2 rounded-md px-3 py-1 selected-option flex-shrink-0 w-25 border mr-1 mb-1">
            <span>{option.nom}</span>
            <button
              className="text-red-600 dark:text-red-400 focus:outline-none"
              onClick={() => handleOptionRemove(option)}
            >
              &times;
            </button>
          </div>
        ))}
    </div>

        <input
          type="text"
          className={`${inputStyle}`}
          value={inputValue}
          placeholder={inputPlaceholder}
          id="input-label"
          autoComplete="off"
          required
          onChange={handleInputChange}
        />

        
        {showIndicator && inputValue !== '' && (
          <div className={`absolute right-4 ${isSelected ? 'top-[65%]' : 'top-1/2'} transform -translate-y-1/2 mt-4`}>
            <Indicator widtf={5} height={5} border="blue" />
          </div>
        )}

      </div>
      <div
        className="custom-input-dropdown w-full"
        style={{ display: isEmpty() ? 'none' : 'block', maxHeight: '200px', overflowY: 'auto' }}
      >
        <ul>
          {listeRoles.length > 0 && isDropdownOpen && inputValue !== '' && (
            <div className={`custom-input-dropdown w-full -mt-1`} style={{ display: isEmpty() ? 'none' : 'block' }}>
              <ul
                style={{ maxHeight: `${maxHeightList}px`, overflowY: 'auto' }}
                className={`absolute top-${topPercentage}% w-full border border-gray-300 rounded-md bg-white shadow-md custom-select-ul`}
              >
                {listeRoles.map((option, index) => (
                  <li
                    className={`dark:border-form-strokedark bg-red dark:bg-form-input px-4 py-1 hover:bg-red-600 cursor-pointer w-full custom-select-li ${
                      isOptionSelected(option) ? 'selected-option' : ''
                    }`}
                    key={index}
                    onClick={() => handleOptionSelect(option)}
                  >
                    {option.nom}
                    {isOptionSelected(option) && <RiCheckLine className="ml-2" />}
                  </li>
                ))}

              </ul>
            </div>
          )}
        </ul>
      </div>
    </div>
  );
};

export default CustomSelectMultiRoles;