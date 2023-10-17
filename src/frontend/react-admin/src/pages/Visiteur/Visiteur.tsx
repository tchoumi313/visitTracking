import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import DefaultLayout from '../../layout/DefaultLayout';
import Breadcrumb from '../../components/Breadcrumb';
import DisplayVisiteurs from './components/DisplayVisiteurs';
import { VisiteurResponse } from '../../generated/models';
import { ReduxProps } from '../../redux/configureStore';
import { TOKEN_LOCAL_STORAGE_KEY } from '../../constants/LOCAL_STORAGE';
import { VisiteurControllerApi } from '../../generated';

import { 
  SuccessNotification,
  DangerNotification,
  WarningNotification,
 } from '../../services/Notification.service';


const Visiteur = () => {

  const state = useSelector((state: ReduxProps) => state);
  const [visiteurs, setVisiteurs] = useState<VisiteurResponse[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const [showSuccessNotif, setShowSuccessNotif] = useState<boolean>(false);
  const [successNotifMessage, setSuccessNotifMessage] = useState<string>('');
  const [successNotifDescription, setSuccessNotifDescription] = useState<string | null>(null);
  
  const [showDangerNotif, setShowDangerNotif] = useState<boolean>(false);
  const [dangerNotifMessage, setDangerNotifMessage] = useState<string>('');
  const [dangerNotifDescription, setDangerNotifDescription] = useState<string | null>(null);
  
  const [showWarning, setShowWarning] = useState<boolean>(false);
  const [warningNotifMessage, setWarningMessage] = useState<string>('');
  const [warningNotifDescription, setWarningNotifDescription] = useState<string | null>(null);


  useEffect(() => {  
    const apiParams: string = localStorage.getItem(TOKEN_LOCAL_STORAGE_KEY)!;
    const visiteursApi = new VisiteurControllerApi({...state.environment, accessToken: apiParams});

    setIsLoading(true)
    
    visiteursApi.index()
    .then((response) => {  
      if(response && response.data){        
        if(response.status === 200){ setVisiteurs(response.data) }
      }
    })
    .catch((error) => {
      alert(error?.response?.data?.message)
    })
    .finally(() => {
      setIsLoading(false)
    });   
  }, []);
  
  return (
    <DefaultLayout>

        {showSuccessNotif && <SuccessNotification message={successNotifMessage} description={successNotifDescription} /> }
        {showDangerNotif && <DangerNotification message={dangerNotifMessage} description={dangerNotifDescription}  />}
        {showWarning && <WarningNotification message={warningNotifMessage} description={warningNotifDescription}  />}

        <Breadcrumb pageName="Visiteurs" />
        <DisplayVisiteurs  
          visiteurs={visiteurs} 
          isLoading={isLoading}
          setShowSuccessNotif={setShowSuccessNotif}
          setSuccessNotifMessage={setSuccessNotifMessage}
          setSuccessNotifDescription={setSuccessNotifDescription}

          setShowDangerNotif={setShowDangerNotif}
          setDangerNotifMessage={setDangerNotifMessage}
          setDangerNotifDescription={setDangerNotifDescription}

          setShowWarning={setShowWarning}
          setWarningMessage={setWarningMessage}
          setWarningNotifDescription={setWarningNotifDescription}
          />
    </DefaultLayout>
  );
};

export default Visiteur;
