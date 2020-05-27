import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import { getFormDataJsonFromEvent } from '../../Utils';
import facebookIcon from '../../assets/img/facebook.svg';
import googleIcon from '../../assets/img/google.svg';
import AwareComponentBuilder from '../../common/AwareComponentBuilder';


const LoginPage = (props) => {

    const history = useHistory();

    const [validationErrors, setValidationErrors] = useState(null);

    const onSubmit = async event => {
        event.preventDefault();
        const formData = getFormDataJsonFromEvent(event);
        const result = await axios.post('/auth/login', formData, { validateStatus: false });
        if (result.status !== 200) {
            setValidationErrors([result.data.msg]);
            return;
        }
        const auth = {
            token: result.data.token,
            tokenExpiry: parseInt(result.data.tokenExpiry),
            email: result.data.email,
            role: result.data.role
        };
        
        const cartItemsResult = await axios.get('/api/cart/raw', {
            headers: { 'X-Auth-Token': auth.token },
            validateStatus: false
        });

        if(cartItemsResult.status !== 200) {
             alert("Unknown error occured.");
             console.log(cartItemsResult);
             return;
        }

        props.logIn(auth);
        props.setCartItems(cartItemsResult.data.map(x => x.productId));

        history.push('/');
    }

    const logWithGoogleOnClick = () => {
        window.location = 'http://localhost:9000/auth/provider/google';
    }

    const logWithFacebookOnClick = () => {
        window.location = 'http://localhost:9000/auth/provider/facebook';
    }

    return <>
        <h3>Sign In</h3>

        <button className="btn btn-primary" onClick={logWithGoogleOnClick}>
            Google
        </button>

        <button className="btn btn-primary" onClick={logWithFacebookOnClick}>
            Facebook
        </button>

        <form onSubmit={onSubmit}>
            <div className="form-group">
                <input name="email" type="email" className="form-control" id="emailInput" placeholder="Email..." required />
            </div>
            <div className="form-group">
                <input name="password" type="password" className="form-control" id="passwordInput" placeholder="Password..." required />
            </div>

            <button type="submit" className="btn btn-primary">
                Submit
            </button>

            {
                validationErrors &&
                <div className="col-12 mt-2">
                    <p className="text-danger font-weight-bold" style={{ marginBottom: '0px' }}>
                        Validation errors
                        </p>
                    <ul style={{ paddingTop: "0" }, { marginTop: "0px" }}>
                        {
                            validationErrors.map((error, i) => {
                                return <li key={`val-err-${i}`} className="text-danger">{error}</li>
                            })
                        }
                    </ul>
                </div>
            }
        </form>
    </>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .withCartAwareness()
    .build(LoginPage);