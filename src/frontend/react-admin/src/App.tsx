import { useEffect, useState } from 'react';
import { Toaster } from 'react-hot-toast';

import Loader from './common/Loader';
import { IS_LOGGED_LOCAL_STORAGE_KEY } from './constants/LOCAL_STORAGE';
import AppSwitch from './pages/AppSwitch/AppSwitch';

function App() {

  const isLoggedIn: boolean = Boolean(localStorage.getItem(IS_LOGGED_LOCAL_STORAGE_KEY));  

  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    setTimeout(() => setLoading(false), 1000);
  }, []);

  return loading ? (
    <Loader />
  ) : (
    <>
      <Toaster position='top-right' reverseOrder={false} containerClassName='overflow-auto'/>
      <AppSwitch isLoggedIn={isLoggedIn}/>
    </>
  );

}

export default App;
