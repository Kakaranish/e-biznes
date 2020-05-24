import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import { getFormDataJsonFromEvent } from '../../Utils';
import { createAuthAwareComponent } from '../../Utils';

const RegisterPage = (props) => {

    const onSubmit = async event => {
        event.preventDefault();
        let formData = getFormDataJsonFromEvent(event);
        if (formData.password !== formData.repeatPassword) {
            setValidationErrors(["passwords are different"])
            return;
        }

        formData.repeatPassword = undefined;
        const result = await axios.post('/auth/register', formData,
            { validateStatus: false });
        if (result.status !== 200) {
            setValidationErrors([result.data ?? "Unknown error"])
            return;
        }
        
        const auth = {
            token: result.data.token,
            tokenExpiry: parseInt(result.data.tokenExpiry),
            email: result.data.email,
            role: result.data.role
        };
        props.logIn(auth);

        setValidationErrors(null);
        history.push('/');
    }

    const history = useHistory();

    const [validationErrors, setValidationErrors] = useState(null);

    useEffect(() => {
        const verifyIfLogged = async () => {
            if (!props.auth?.token) return;
            if (props.auth?.tokenExpiry > Date.now()) {
                props.logOut();
                return;
            }

            const result = await axios.post('/auth/verify', {}, {
                headers: { 'X-Auth-Token': props.auth.token },
                validateStatus: false
            });
            if (result.status === 200) {
                alert("You' are already logged in");
                history.push('/');
                return;
            }
            props.logOut();
        };
        verifyIfLogged();
    }, []);

    return <>
        <h3>Sign In</h3>
        <form onSubmit={onSubmit}>
            <div className="form-group">
                <input name="email" type="email" className="form-control" id="emailInput" placeholder="Email..." required />
            </div>
            <div className="form-group">
                <input name="firstName" type="text" className="form-control"
                    id="firstNameInput" placeholder="First name..." required />
            </div>
            <div className="form-group">
                <input name="lastName" type="text" className="form-control"
                    id="lastNameInput" placeholder="Last name..." required />
            </div>
            <div className="form-group">
                <input name="password" type="password" className="form-control"
                    id="passwordInput" placeholder="Password..." required />
            </div>

            <div className="form-group">
                <input name="repeatPassword" type="password" className="form-control"
                    id="repepatPasswordInput" placeholder="Repeat password..." required />
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

export default createAuthAwareComponent(RegisterPage);