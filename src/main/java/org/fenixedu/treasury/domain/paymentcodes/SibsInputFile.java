/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: ricardo.pedro@qub-it.com, anil.mamede@qub-it.com
 * 
 *
 * 
 * This file is part of FenixEdu Treasury.
 *
 * FenixEdu Treasury is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Treasury is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Treasury.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.treasury.domain.paymentcodes;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class SibsInputFile extends SibsInputFile_Base {

    public static final String CONTENT_TYPE = "text/plain";

    protected SibsInputFile() {
        super();
    }

    protected SibsInputFile(FinantialInstitution finantialInstitution, DateTime whenProcessedBySIBS, String displayName,
            String filename, byte[] content, User uploader) {
        this();
        init(finantialInstitution, whenProcessedBySIBS, displayName, filename, content, uploader);

    }

    protected void init(FinantialInstitution finantialInstitution, DateTime whenProcessedBySIBS, String displayName,
            String filename, byte[] content, User uploader) {
        super.init(displayName, filename, content);
        setWhenProcessedBySibs(whenProcessedBySIBS);
        setUploader(uploader);
        setFinantialInstitution(finantialInstitution);
        checkRules();
    }

    private void checkRules() {
        //
        //CHANGE_ME add more busines validations
        //

        //CHANGE_ME In order to validate UNIQUE restrictions
    }

    @Atomic
    public void edit() {
        checkRules();
    }

    public boolean isDeletable() {
        return true;
    }

    @Override
    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.SibsInputFile.cannot.delete");
        }

        setFinantialInstitution(null);
        setUploader(null);
        super.delete();
    }

    @Atomic
    public static SibsInputFile create(FinantialInstitution finantialInstitution, DateTime whenProcessedBySIBS,
            String displayName, String filename, byte[] content, User uploader) {
        return new SibsInputFile(finantialInstitution, whenProcessedBySIBS, displayName, filename, content, uploader);

    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<SibsInputFile> findAll() {
        Set<SibsInputFile> result = new HashSet<SibsInputFile>();
        for (FinantialInstitution finantialInstitution : FinantialInstitution.findAll().collect(Collectors.toList())) {
            result.addAll(finantialInstitution.getSibsInputFilesSet());
        }
        return result.stream();
    }

    public static Stream<SibsInputFile> findByUploader(final User uploader) {
        return findAll().filter(i -> uploader.equals(i.getUploader()));
    }

    @Override
    public boolean isAccessible(User arg0) {
        return true;
    }

}
