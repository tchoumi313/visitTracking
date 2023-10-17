import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import DefaultLayout from '../../layout/DefaultLayout';
import Breadcrumb from '../../components/Breadcrumb';
import { BureauResponse } from '../../generated/models';
import { ReduxProps } from '../../redux/configureStore';
import { TOKEN_LOCAL_STORAGE_KEY } from '../../constants/LOCAL_STORAGE';
import { BureauControllerApi } from '../../generated';

import {
  SuccessNotification,
  DangerNotification,
  WarningNotification,
} from '../../services/Notification.service';
import DisplayBureaux from './components/DisplayBureaux';


const Bureau = () => {

  const state = useSelector((state: ReduxProps) => state);
  const [bureaux, setBureaux] = useState<BureauResponse[]>([]);
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
    const bureauxApi = new BureauControllerApi({ ...state.environment, accessToken: apiParams });

    setIsLoading(true)

    bureauxApi.index3()
      .then((response) => {
        if (response && response.data) {
          if (response.status === 200) { setBureaux(response.data) }
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

      {showSuccessNotif && <SuccessNotification message={successNotifMessage} description={successNotifDescription} />}
      {showDangerNotif && <DangerNotification message={dangerNotifMessage} description={dangerNotifDescription} />}
      {showWarning && <WarningNotification message={warningNotifMessage} description={warningNotifDescription} />}

      <Breadcrumb pageName="Bureaux" />
      <DisplayBureaux
        bureaux={bureaux}
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

export default Bureau;
